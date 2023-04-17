package homemade.game.pipeline.stages

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.pipeline.PipelineStage
import homemade.game.pipeline.ProcessingInfo
import homemade.game.state.MutableGameState
import homemade.game.state.MutableSelectionState

class SelectionProcessingStage : PipelineStage() {
    override fun process(state: MutableGameState, processingInfo: ProcessingInfo) {
        TODO("Not yet implemented")
    }


    fun updateSelectionState() {
        val iterator: MutableIterator<CellCode> = selection.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (!cellMapReader.getCell(next).isAliveBlock()) {
                iterator.remove()
            }
        }
        val copy: HashSet<CellCode> = HashSet<CellCode>(selection)
        val cellsToMove: HashSet<CellCode> = HashSet<CellCode>(fieldSize)
        if (selection.size > 1) {
            throw RuntimeException("accessible cells are undefined")
        } else if (selection.size == 1) {
            val unaccessableCells: HashSet<CellCode> = HashSet<CellCode>(fieldSize)
            var borderCells = HashSet<CellCode>(1)
            val directions = Direction.values()
            borderCells.add(selection.get(0))
            while (!borderCells.isEmpty()) {
                val newBorder = HashSet<CellCode>(Math.min(borderCells.size * 2, fieldSize / 2))
                for (borderCell in borderCells) {
                    unaccessableCells.add(borderCell)
                    for (direction in directions) {
                        val neighbour = borderCell.neighbour(direction)
                        if (neighbour != null && !unaccessableCells.contains(neighbour)) {
                            if (cellMapReader.getCell(neighbour).isFreeForMove()) {
                                newBorder.add(neighbour)
                                cellsToMove.add(neighbour)
                            } else {
                                unaccessableCells.add(neighbour)
                            }
                        }
                    }
                }
                borderCells = newBorder
            }
        }
        state = MutableSelectionState(copy, cellsToMove)
    }
}
