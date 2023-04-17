package homemade.game.pipeline

import homemade.game.state.FieldState

internal class BlockProcessingStage : PipelineStage() {
    override fun process(state: FieldState, processingInfo: ProcessingInfo) {
        TODO("Not yet implemented")

        //note: else if (state.getNumberOfMovableBlocks() == 0) might happen if a) field is empty or b) field is stuck
    }
}
