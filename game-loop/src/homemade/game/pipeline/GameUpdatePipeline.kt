package homemade.game.pipeline

import homemade.game.loop.*
import homemade.game.model.GameSettings
import homemade.game.pipeline.stages.*
import homemade.game.state.MutableGameState

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

    init {
        gameLoop.model.subscribe<RequestBlockSpawning>(this)
        gameLoop.model.subscribe<CreateSnapshot>(this)
        gameLoop.model.subscribe<UserClick>(this)

        when (mutableGameState.configState.settings.gameMode) {
            GameSettings.GameMode.REAL_TIME -> RegularSpawnPipeline(mutableGameState, gameLoop.model)
            GameSettings.GameMode.TURN_BASED -> TurnBasedInitPipeline(gameLoop.model)
        }
    }

    override fun handle(event: GameEvent) {
        when (event) {
            is RequestBlockSpawning -> handleBlockSpawning(event)
            is CreateSnapshot -> handleCreateSnapshot(event)
            is UserClick -> handleUserInput(event)
            else -> throw RuntimeException("bad subscription $event")
        }
    }

    private fun handleUserInput(event: UserClick) {
        val processingInfo = ProcessingInfo(event)
        UserInputProcessingStage().process(mutableGameState, processingInfo)
    }

    private fun handleBlockSpawning(event: RequestBlockSpawning) {
        //don't necessarily post to ui loop, but next snapshot must be aware of the results
        //does it mean that we're the one who makes them?
        val previousStats = mutableGameState.configState.copyConfigState()

        val processingInfo = ProcessingInfo(event)
        do {
            BlockProcessingStage().process(mutableGameState, processingInfo)
            LinkProcessingStage().process(mutableGameState, processingInfo)
            ComboProcessingStage().process(mutableGameState, processingInfo)
        } while ("combo processing generated extra block updates" == "yea")
        SelectionProcessingStage().process(mutableGameState, processingInfo)

        // prolly just leave it at that
        // and send the current mutable state through gamestateencoder, if snapshot is requested

        if (previousStats.globalMultiplier != mutableGameState.configState.globalMultiplier) {
            uiLoop.post(MultiplierChanged(mutableGameState.configState.globalMultiplier - previousStats.globalMultiplier))
        }
    }

    private fun handleCreateSnapshot(event: CreateSnapshot) {
        uiLoop.post(SnapshotReady(mutableGameState.createImmutable()))
    }
}
