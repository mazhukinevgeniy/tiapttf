package homemade.game.pipeline

import homemade.game.loop.*
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
class FieldUpdatePipeline(val uiLoop: UILoop, private val mutableGameState: MutableGameState) : GameEventHandler<GameEvent> {

    override fun handle(event: GameEvent) {
        when (event) {
            is BatchedBlockChange -> handleBatchedBlockChange(event)
            is CreateSnapshot -> handleCreateSnapshot(event)
            else -> throw RuntimeException("bad subscription $event")
        }
    }

    private fun handleBatchedBlockChange(event: BatchedBlockChange) {
        //don't necessarily post to ui loop, but next snapshot must be aware of the results
        //does it mean that we're the one who makes them?
        TODO("impl")

        val processingInfo = ProcessingInfo(emptySet(), event.reason)
        do {
            BlockProcessingStage().process(mutableGameState, processingInfo)
            LinkProcessingStage().process(mutableGameState, processingInfo)
            ComboProcessingStage().process(mutableGameState, processingInfo)
        } while ("combo processing generated extra block updates" == "yea")
        SelectionProcessingStage().process(mutableGameState, processingInfo)

        // prolly just leave it at that
        // and send the current mutable state through gamestateencoder, if snapshot is requested
    }

    private fun handleCreateSnapshot(event: CreateSnapshot) {
        TODO("impl")
    }
}
