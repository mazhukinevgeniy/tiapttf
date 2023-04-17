package homemade.game.pipeline.stages

import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.state.MutableGameState

class BlockProcessingStage : PipelineStage() {
    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        TODO("Not yet implemented")

        //note: else if (state.getNumberOfMovableBlocks() == 0) might happen if a) field is empty or b) field is stuck
    }
}
