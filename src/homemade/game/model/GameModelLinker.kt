package homemade.game.model

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.FieldStructure
import homemade.game.loop.*
import homemade.game.model.GameSettings.GameMode
import homemade.game.state.impl.CellMap
import homemade.game.model.cellmap.CellMapReader
import homemade.game.model.cellstates.SimpleState
import homemade.game.model.combo.ComboDetector
import homemade.game.model.combo.ComboEffectVendor
import homemade.game.model.spawn.SpawnManager
import homemade.game.scenarios.GameOverScenario
import homemade.game.scenarios.UserInputScenario
import homemade.game.state.ConfigState
import homemade.game.state.GameState
import homemade.game.state.MutableFieldState
import homemade.game.state.MutableGameState
import homemade.game.state.immutable.GameStateEncoder
import homemade.game.state.impl.BlockSelection
import homemade.game.state.impl.BlockValuePool
import java.util.*

class GameModelLinker(val structure: FieldStructure, val settings: GameSettings, private val gameLoop: GameLoop) : GameEventHandler<GameEvent> {
    private val cellMap: CellMap
    private val spawner: SpawnManager
    var state: ArrayBasedFieldState
    private val storedEffects: LinkedList<ComboEffect>
    var selection: BlockSelection
    private val updater: Updater
    private var lastGameState: GameState
    private val trueState = MutableGameState(MutableFieldState(), selection, ConfigState())

    init {
        new FieldUpdatePipeline(gameLoop, trueState)
        val blockValuePool = BlockValuePool(settings.maxBlockValue, structure.fieldSize)
        cellMap = CellMap(structure, blockValuePool)
        storedEffects = LinkedList()
        state = ArrayBasedFieldState(structure)
        val comboDetector = ComboDetector(this, gameLoop.ui)
        val gameScore = GameScore(this)
        updater = Updater(this, comboDetector, cellMap, gameScore, state)
        spawner = SpawnManager(this, blockValuePool)
        selection = BlockSelection(this)
        lastGameState = GameState(GameStateEncoder().encode(state), selection.copySelectionState())
        val mode = settings.gameMode
        GameOverScenario(gameLoop, this)
        UserInputScenario(gameLoop, selection)
        gameLoop.model.subscribe<CreateSnapshot>(this)
    }

    override fun handle(event: GameEvent) {
        if (event is CreateSnapshot) {
            gameLoop.ui.post(SnapshotReady(trueState.createImmutable()))
        } else {
            throw RuntimeException("unknown $event")
        }
    }

    @get:Synchronized
    val mapReader: CellMapReader
        get() = cellMap

    @Synchronized
    fun killRandomBlocks() {
        updater.takeChanges(spawner.spawnDeadBlocks())
        updateStates()
    }

    @Synchronized
    fun updateScore(newScore: Int) {
        state.updateScore(newScore)
    }

    @Synchronized
    fun modifyGlobalMultiplier(change: Int) {
        val oldMultiplier = trueState.configState.globalMultiplier
        val rawMultiplier = oldMultiplier + change
        val newMultiplier = Math.max(1, rawMultiplier)
        if (oldMultiplier != newMultiplier) {
            state.updateMultiplier(newMultiplier)
            gameLoop.ui.post(MultiplierChanged(change))
        }
    }

    @Synchronized
    fun requestSpawn() {
        modifyGlobalMultiplier(-1)
        updater.takeComboChanges(spawner.spawnBlocks())
        updater.takeChanges(spawner.markCellsForSpawn())
        updateStates()
    }

    fun tryMove(moveFromCell: CellCode, moveToCell: CellCode) {
        val repercussions = cellMap.getCell(moveToCell).type() == Cell.MARKED_FOR_SPAWN &&
                trueState.configState.globalMultiplier == 1
        val cellFrom = cellMap.getCell(moveFromCell)
        val cellTo = cellMap.getCell(moveToCell)
        if (cellTo.isFreeForMove && cellFrom.isMovableBlock) {
            state.incrementDenyCounter()
            val tmpMap: MutableMap<CellCode, CellState> = HashMap()
            tmpMap[moveFromCell] = SimpleState.getSimpleState(if (repercussions) Cell.DEAD_BLOCK else Cell.EMPTY)
            tmpMap[moveToCell] = cellFrom
            updater.takeComboChanges(tmpMap)
            if (settings.gameMode === GameMode.TURN_BASED && !updater.hasCombos()) {
                requestSpawn()
            } else {
                updateStates()
            }
        }
    }

    private fun updateStates() {
        if (updater.hasCellChanges()) {
            val comboPackTier = updater.comboPackTier()
            ComboEffectVendor().addComboEffectsForTier(storedEffects, comboPackTier)
            updater.takeChanges(spawner.markBlocksWithEffects(storedEffects))
            trueState.selectionState.updateSelectionState()
            updater.flush(trueState.configState.globalMultiplier)
        }
        if (state.getNumberOfBlocks() == structure.fieldSize) {
            val multiplier = trueState.configState.globalMultiplier
            if (multiplier > 1) {
                modifyGlobalMultiplier(-multiplier)
                updater.takeChanges(spawner.removeRandomBlocks())
                updateStates()
                println("multiplier consumed")
            } else {
                gameLoop.model.post(GameOver(1))
                println("can't trade multiplier for blocks")
            }
        } else if (state.getNumberOfMovableBlocks() == 0) {
            modifyGlobalMultiplier(BONUS_MULTIPLIER_FOR_BOARD_CLEAR + INITIAL_SPAWNS)
            for (i in 0 until INITIAL_SPAWNS) {
                requestSpawn()
            }
        }
    }

    companion object {
        private const val BONUS_MULTIPLIER_FOR_BOARD_CLEAR = 50
        private const val INITIAL_SPAWNS = 2
    }
}
