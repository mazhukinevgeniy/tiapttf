package homemade.game.pipeline.stages

import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.state.MutableGameState

class LinkProcessingStage : PipelineStage() {
    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        // no-op, because CellMap does everything. supposedly
        // TODO: would be nice to have a dumber model
    }
}
