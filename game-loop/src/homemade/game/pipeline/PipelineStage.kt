package homemade.game.pipeline

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.LinkCode
import homemade.game.loop.GameEvent
import homemade.game.model.CellState
import homemade.game.state.MutableGameState

data class ProcessingInfo(
        var sourceEvent: GameEvent,
        var updatedCells: Map<CellCode, CellState> = HashMap(),
        var updatedLinks: Map<LinkCode, Direction> = HashMap(),
        var updatedChains: Map<LinkCode, Int> = HashMap()
)

abstract class PipelineStage {
    abstract fun process(state: MutableGameState, processingInfo: ProcessingInfo)
}
