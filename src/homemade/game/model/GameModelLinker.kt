package homemade.game.model

import homemade.game.fieldstructure.FieldStructure
import homemade.game.loop.*
import homemade.game.model.combo.ComboDetector
import homemade.game.model.combo.ComboEffectVendor
import homemade.game.model.spawn.SpawnManager
import homemade.game.pipeline.GameUpdatePipeline
import homemade.game.scenarios.GameOverScenario
import homemade.game.state.*
import homemade.game.state.impl.BlockValuePool
import java.util.*

class GameModelLinker(val structure: FieldStructure, val settings: GameSettings, private val gameLoop: GameLoop) {
    private val spawner: SpawnManager
    private val storedEffects: LinkedList<ComboEffect>
    private val updater: Updater

    init {
        GameUpdatePipeline(
                gameLoop,
                MutableGameState(
                        MutableFieldState(structure, BlockValuePool(settings.maxBlockValue, structure.fieldSize)),
                        MutableSelectionState(null, HashSet()),
                        MutableConfigState(settings, 0, 0, 1)
                )
        )
        storedEffects = LinkedList()
        val comboDetector = ComboDetector(this, gameLoop.ui)
        val gameScore = GameScore(this)
        updater = Updater(this, comboDetector, cellMap, gameScore, state)
        spawner = SpawnManager(this, blockValuePool)
        GameOverScenario(gameLoop, this)
    }

    @Synchronized
    fun killRandomBlocks() {
        updater.takeChanges(spawner.spawnDeadBlocks())
        updateStates()
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
