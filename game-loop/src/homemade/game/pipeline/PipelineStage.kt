package homemade.game.pipeline

import homemade.game.fieldstructure.CellCode
import homemade.game.loop.ChangeReason
import homemade.game.state.GameState

data class ProcessingInfo(
        var triggeredCells: Set<CellCode>,
        var reason: ChangeReason
)

abstract class PipelineStage {
    abstract fun process(state: GameState, processingInfo: ProcessingInfo)
}
