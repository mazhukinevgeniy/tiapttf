package homemade.game.pipeline

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.LinkCode
import homemade.game.state.MutableGameState

data class ProcessingInfo(
        var triggeredCells: Set<CellCode> = emptySet(),
        var triggeredLinks: Set<LinkCode> = emptySet()
)

abstract class PipelineStage {
    abstract fun process(state: MutableGameState, processingInfo: ProcessingInfo)
}
