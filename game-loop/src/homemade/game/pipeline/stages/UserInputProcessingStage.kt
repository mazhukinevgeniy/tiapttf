package homemade.game.pipeline.stages

import homemade.game.fieldstructure.CellCode
import homemade.game.loop.UserClick
import homemade.game.model.cellstates.SimpleState
import homemade.game.pipeline.ChangedData
import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.state.MutableGameState

class UserInputProcessingStage : PipelineStage() {
    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        val event = processingInfo.sourceEvent as UserClick? ?: return

        val eventCell = event.cellCode
        if (state.fieldState.getCellState(eventCell).isMovableBlock) {
            state.selectionState.selection = eventCell
            processingInfo.changedData.add(ChangedData.SELECTION)
        } else {
            val currentSelection = state.selectionState.selection
            if (currentSelection?.let { it in state.selectionState.cellsToMove } == true) {
                tryMove(currentSelection, eventCell, state, processingInfo)
            }
        }
    }

    private fun tryMove(moveFromCell: CellCode, moveToCell: CellCode, state: MutableGameState, processingInfo: ProcessingInfo) {
        check(moveFromCell != moveToCell)

        val repercussions = cellMap.getCell(moveToCell).type() == Cell.MARKED_FOR_SPAWN &&
                trueState.configState.globalMultiplier == 1
        val cellFrom = cellMap.getCell(moveFromCell)
        val cellTo = cellMap.getCell(moveToCell)
        if (cellTo.isFreeForMove && cellFrom.isMovableBlock) {
            state.incrementDenyCounter()
            val tmpMap: MutableMap<CellCode, CellState> = HashMap()
            tmpMap[moveFromCell] = SimpleState.getSimpleState(if (repercussions) Cell.DEAD_BLOCK else Cell.EMPTY)
            tmpMap[moveToCell] = cellFrom
            updater.takeComboChanges(tmpMap)
            if (settings.gameMode === GameMode.TURN_BASED && !updater.hasCombos()) {
                requestSpawn()
            } else {
                updateStates()
            }
        }

        // after move is resolved.
        // this feature helps cross 'crumbling bridges' in real-time mode. or correct a misclick, I guess
        if (state.fieldState.getCellState(moveToCell).isAliveBlock) {
            state.selectionState.selection = moveToCell
        }
    }
}
