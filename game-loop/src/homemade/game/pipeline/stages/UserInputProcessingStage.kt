package homemade.game.pipeline.stages

import homemade.game.fieldstructure.CellCode
import homemade.game.loop.GameEvent
import homemade.game.loop.UserClick
import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.state.MutableGameState

class UserInputProcessingStage : PipelineStage() {
    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        TODO("Not yet implemented")
    }

    fun handle(event: GameEvent?) {
        if (event !is UserClick) {
            return
        }
        val eventCell = event.cellCode
        if (cellMapReader.getCell(eventCell).isMovableBlock()) {
            selection.clear()
            selection.add(eventCell)
            updateSelectionState()
        } else if (selection.size == 1 && state.canMoveTo(eventCell)) {
            val selectedCell: CellCode = selection.get(0)
            tryMove(selectedCell, eventCell)
        }
    }

    private fun tryMove(selectedCell: CellCode, eventCell: CellCode) {
        if (eventCell !== selectedCell) {
            linker.tryMove(selectedCell, eventCell)
            selection.clear()
            val selectedCellOccupied: Boolean = cellMapReader.getCell(selectedCell).isAliveBlock()
            val eventCellOccupied: Boolean = cellMapReader.getCell(eventCell).isAliveBlock()
            if (selectedCellOccupied) {
                selection.add(eventCell)
            } else if (eventCellOccupied) {
                selection.add(eventCell)
            }
            updateSelectionState()
        }
    }
}
