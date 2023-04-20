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
            if (state.selectionState.selection != eventCell) {
                state.changeSelection().selection = eventCell
            }
        } else if (eventCell in state.selectionState.cellsToMove) {
            val selection = state.selectionState.selection
            check(selection != null) { "inconsistent selection state ${state.selectionState}" }
            tryMove(selection, eventCell, state, processingInfo)
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
            val stateBehind = SimpleState.get(if (repercussions) Cell.DEAD_BLOCK else Cell.EMPTY)

            CellMarker(state, processingInfo).execute(mapOf(
                    moveToCell to cellFrom,
                    moveFromCell to stateBehind
            ))

            // this feature helps cross 'crumbling bridges' in real-time mode. or correct a misclick
            state.changeSelection().selection = moveToCell
        }
    }
}
