package homemade.game.pipeline.stages

import homemade.game.fieldstructure.CellCode
import homemade.game.loop.UserClick
import homemade.game.model.Cell
import homemade.game.model.cellstates.SimpleState
import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.pipeline.operations.CellMarker
import homemade.game.state.MutableGameState

class UserInputProcessingStage : PipelineStage() {
    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        val event = processingInfo.sourceEvent as UserClick? ?: return

        val eventCell = event.cellCode
        if (state.fieldState.getCellState(eventCell).isMovableBlock) {
            state.changeSelection().selection = eventCell
        } else {
            val currentSelection = state.selectionState.selection
            if (currentSelection?.let { it in state.selectionState.cellsToMove } == true) {
                tryMove(currentSelection, eventCell, state, processingInfo)
            }
        }
    }

    private fun tryMove(moveFromCell: CellCode, moveToCell: CellCode, state: MutableGameState, processingInfo: ProcessingInfo) {
        check(moveFromCell != moveToCell)

        val cellFrom = state.fieldState.getCellState(moveFromCell)
        val cellTo = state.fieldState.getCellState(moveToCell)

        if (cellTo.isFreeForMove && cellFrom.isMovableBlock) {
            val repercussions = cellTo.type() == Cell.MARKED_FOR_SPAWN && state.configState.globalMultiplier == 1
            if (repercussions) {
                state.changeConfig().spawnsDenied++
            }
            val stateBehind = SimpleState.getSimpleState(if (repercussions) Cell.DEAD_BLOCK else Cell.EMPTY)

            CellMarker(state, processingInfo).execute(mapOf(
                    moveToCell to cellFrom,
                    moveFromCell to stateBehind
            ))
            // this feature helps cross 'crumbling bridges' in real-time mode. or correct a misclick, I guess
            // TODO make sure that it's invalidated properly if cell is consumed in combo
            state.changeSelection().selection = moveToCell

            //TODO takeComboChanges whatever it means
        }
    }
}
