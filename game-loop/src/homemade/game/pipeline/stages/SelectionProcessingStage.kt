package homemade.game.pipeline.stages

import homemade.game.fieldstructure.CellCode
import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.state.MutableGameState

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

        val visited = hashSetOf(currentSelection)
        val accessible = HashSet<CellCode>()
        val toCheck = currentSelection.vicinity.toMutableList()
        while (toCheck.isNotEmpty()) {
            val next = toCheck.removeAt(toCheck.size - 1)
            if (next !in visited) {
                visited.add(next)
                if (state.fieldState.getCellState(next).isFreeForMove) {
                    accessible.add(next)
                    for (item in next.vicinity) {
                        if (item !in visited) {
                            toCheck.add(item)
                        }
                    }
                }
            }
        }
        if (accessible != currentCellsToMove) {
            val mutableSelection = state.changeSelection()
            mutableSelection.cellsToMove = accessible
        }
    }
}
