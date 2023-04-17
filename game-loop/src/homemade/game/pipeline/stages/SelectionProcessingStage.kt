package homemade.game.pipeline.stages

import homemade.game.fieldstructure.CellCode
import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.state.MutableGameState

class SelectionProcessingStage : PipelineStage() {
    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        val currentSelection = state.selectionState.selection
        val currentCellsToMove = state.selectionState.cellsToMove

        val leftoverList = currentSelection.filter { state.fieldState.getCellState(it)?.isAliveBlock ?: false }
        if (leftoverList.size < currentSelection.size) {
            state.isDirtySelection = true
            state.selectionState.selection = leftoverList.toHashSet()
        }
        if (leftoverList.size > 1) {
            throw RuntimeException("broken selection $leftoverList")
        }

        val visited = HashSet<CellCode>()
        val accessible = HashSet<CellCode>()
        val toCheck = leftoverList.elementAtOrNull(0)?.vicinity?.toMutableList() ?: ArrayList(0)
        while (toCheck.isNotEmpty()) {
            val next = toCheck.removeAt(toCheck.size - 1)
            if (next !in visited) {
                visited.add(next)
                if (state.fieldState.getCellState(next)?.isFreeForMove == true) {
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
            state.isDirtySelection = true
            state.selectionState.cellsToMove = accessible
        }
    }
}
