package homemade.game.pipeline

import homemade.game.fieldstructure.FieldStructure
import homemade.game.loop.*
import homemade.game.model.GameSettings
import homemade.game.pipeline.operations.CellMarker
import homemade.game.pipeline.stages.*
import homemade.game.pipeline.starters.RegularSpawnPipeline
import homemade.game.pipeline.starters.TurnBasedInitPipeline
import homemade.game.state.MutableConfigState
import homemade.game.state.MutableFieldState
import homemade.game.state.MutableGameState
import homemade.game.state.MutableSelectionState
import homemade.game.state.impl.BlockValuePool

/**
 * |----
 * | either user clicks cell
 * | or some blocks spawn
 * |----
 * ->
 * |----
 * | 1. blocks change (potentially creating a combo, and causing removal of some blocks; so step 1 might repeat a few times)
 * | 2. selection state changes
 * | 3. links change (blocks become more/less connected)
 * |
 * | lesser parts of state change: multiplier, state
 * |----
 */
class GameUpdatePipeline(gameLoop: GameLoop, private val mutableGameState: MutableGameState) : GameEventHandler<GameEvent> {
    private val uiLoop = gameLoop.ui
    private val gameEventPoster: EventPoster<GameEvent> = gameLoop.model

    init {
        gameLoop.model.subscribe<RequestBlockSpawning>(this)
        gameLoop.model.subscribe<CreateSnapshot>(this)
        gameLoop.model.subscribe<UserClick>(this)
        gameLoop.model.subscribe<GameOver>(this)

        when (mutableGameState.configState.settings.gameMode) {
            GameSettings.GameMode.REAL_TIME -> RegularSpawnPipeline(mutableGameState, gameLoop.model)
            GameSettings.GameMode.TURN_BASED -> TurnBasedInitPipeline(gameLoop.model)
        }
    }

    override fun handle(event: GameEvent) {
        when (event) {
            is RequestBlockSpawning -> handleBlockSpawning(event)
            is CreateSnapshot -> handleCreateSnapshot()
            is UserClick -> handleUserInput(event)
            is GameOver -> handleGameOver(event)
            else -> throw RuntimeException("bad subscription $event")
        }
    }

    private fun handleUserInput(event: UserClick) {
        val processingInfo = ProcessingInfo(event, mutableGameState.changeConfig().copyConfigState())

        UserInputProcessingStage().process(mutableGameState, processingInfo)
        ComboProcessingStage(uiLoop).process(mutableGameState, processingInfo)

        val madeNoCombos = processingInfo.storedCombos.packTier() == 0
        val turnBased = mutableGameState.configState.settings.gameMode == GameSettings.GameMode.TURN_BASED
        val movedBlocks = processingInfo.comboStarts.isNotEmpty()

        if (turnBased && madeNoCombos && movedBlocks) {
            gameEventPoster.post(RequestBlockSpawning(1))
        }

        commonCleanup(processingInfo)
    }

    private fun handleBlockSpawning(event: RequestBlockSpawning) {
        val processingInfo = ProcessingInfo(event, mutableGameState.changeConfig().copyConfigState())

        SpawnProcessingStage().process(mutableGameState, processingInfo)
        ComboProcessingStage(uiLoop).process(mutableGameState, processingInfo)

        commonCleanup(processingInfo)
    }

    private fun commonCleanup(processingInfo: ProcessingInfo) {
        ScoreUpdateStage().process(mutableGameState, processingInfo)

        BoardStateCheckStage(gameEventPoster).process(mutableGameState, processingInfo)

        SelectionProcessingStage().process(mutableGameState, processingInfo)
        EffectSpawningStage().process(mutableGameState, processingInfo)

        val multiplierBefore = processingInfo.initialConfigState.globalMultiplier
        val multiplierNow = mutableGameState.configState.globalMultiplier

        if (multiplierBefore != multiplierNow) {
            uiLoop.post(MultiplierChanged(multiplierNow - multiplierBefore))
        }
    }

    private fun handleGameOver(gameOver: GameOver) {
        if (gameOver.countdown == 0) {
            uiLoop.post(ShutDown)
        } else {
            val info = ProcessingInfo(gameOver, mutableGameState.changeConfig().copyConfigState())
            CellMarker(mutableGameState, info).spawnDeadBlocks()
            gameEventPoster.post(DelayedEvent(200, GameOver(gameOver.countdown - 1)))
        }
    }

    private fun handleCreateSnapshot() {
        uiLoop.post(SnapshotReady(mutableGameState.createImmutable()))
    }

    companion object {
        fun create(structure: FieldStructure, settings: GameSettings, gameLoop: GameLoop): GameUpdatePipeline {
            return GameUpdatePipeline(
                    gameLoop,
                    MutableGameState(
                            MutableFieldState(structure, BlockValuePool(settings.maxBlockValue, structure.fieldSize)),
                            MutableSelectionState(null, HashSet()),
                            MutableConfigState(settings, 0, 0, 1)
                    )
            )
        }
    }
}
