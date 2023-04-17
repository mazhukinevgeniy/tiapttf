package homemade.game.pipeline

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.LinkCode
import homemade.game.state.MutableGameState

data class ProcessingInfo(
        var triggeredCells: Set<CellCode>,
        var triggeredLinks: Set<LinkCode>
)

abstract class PipelineStage {
    abstract fun process(state: MutableGameState, processingInfo: ProcessingInfo)
}
