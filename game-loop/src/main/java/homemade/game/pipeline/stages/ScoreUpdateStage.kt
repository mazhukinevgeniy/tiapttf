package homemade.game.pipeline.stages

import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.pipeline.operations.GameScore
import homemade.game.state.MutableGameState

class ScoreUpdateStage : PipelineStage() {

    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        GameScore().handleCombos(processingInfo.storedCombos, state.changeConfig())
    }
}
