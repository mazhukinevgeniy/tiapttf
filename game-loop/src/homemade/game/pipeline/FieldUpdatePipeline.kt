package homemade.game.pipeline

import homemade.game.loop.*

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
class FieldUpdatePipeline(val uiLoop: UILoop) : GameEventHandler<GameEvent> {

    override fun handle(event: GameEvent) {
        when (event) {
            is BatchedBlockChange -> handleBatchedBlockChange(event)
            is CreateSnapshot -> handleCreateSnapshot(event)
            else -> throw RuntimeException("bad subscription $event")
        }
    }

    private fun handleBatchedBlockChange(event: BatchedBlockChange) {
        val updateSummary = null

        //don't necessarily post to ui loop, but next snapshot must be aware of the results
        //does it mean that we're the one who makes them?
        TODO("impl")
    }

    private fun handleCreateSnapshot(event: CreateSnapshot) {
        TODO("impl")
    }
}
