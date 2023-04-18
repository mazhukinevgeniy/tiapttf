package homemade.game.pipeline.stages

import homemade.game.loop.RequestBlockSpawning
import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.pipeline.operations.CellMarker
import homemade.game.state.MutableGameState

class SpawnProcessingStage : PipelineStage() {
    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        val event = processingInfo.sourceEvent as RequestBlockSpawning? ?: return

        state.changeConfig().globalMultiplier--

        val marker = CellMarker(state, processingInfo)

        repeat(event.weight) {
            marker.spawnBlocks()
            marker.markCellsForSpawn()
        }
    }
}
