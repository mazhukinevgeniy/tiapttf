package homemade.game.view.layers

import homemade.game.fieldstructure.CellCode
import homemade.game.model.Cell
import homemade.game.state.canMoveTo
import homemade.game.state.isSelected
import homemade.game.view.layers.RenderingLayer.Cells
import homemade.resources.blocks.BlockAssets
import java.awt.Image

internal class BlockLayer : Cells() {
    private val blockAssets: BlockAssets = assets.blockAssets

    override fun renderForCell(cellCode: CellCode) {
        if (selectionState.canMoveTo(cellCode)) {
            graphics.drawImage(assets.placeToMove, canvasX, canvasY, null)
        }
        val cellState = state.getCellState(cellCode)
        val sprite: Image = when (cellState.type()) {
            Cell.EMPTY -> return
            Cell.MARKED_FOR_SPAWN -> assets.smallBlock
            Cell.DEAD_BLOCK -> blockAssets.deadBlock
            Cell.OCCUPIED -> blockAssets.getBlock(
                    cellState.isMovableBlock,
                    selectionState.isSelected(cellCode),
                    cellState.effect()
            )

            else -> throw RuntimeException("unknown cell type ${cellState.type()}")
        }
        graphics.drawImage(sprite, canvasX, canvasY, null)
    }
}
