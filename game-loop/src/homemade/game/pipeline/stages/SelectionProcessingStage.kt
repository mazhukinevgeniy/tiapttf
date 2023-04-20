package homemade.game.pipeline.stages

import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.state.MutableGameState
import homemade.game.state.impl.ReachableCellsCalculator

class SelectionProcessingStage : PipelineStage() {
    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        val currentSelection = state.selectionState.selection
        val currentCellsToMove = state.selectionState.cellsToMove

        if (currentSelection == null) {
            check(currentCellsToMove.isEmpty()) { "can't have possible moves if nothing is selected" }
            return
        }

        if (!state.fieldState.getCellState(currentSelection).isMovableBlock) {
            val mutableSelection = state.changeSelection()
            mutableSelection.selection = null
            mutableSelection.cellsToMove = HashSet()
            return
        }

        val accessible = ReachableCellsCalculator().buildReachableCellSet(state.fieldState, currentSelection)

        if (accessible != currentCellsToMove) {
            val mutableSelection = state.changeSelection()
            mutableSelection.cellsToMove = accessible
        }
    }
}
