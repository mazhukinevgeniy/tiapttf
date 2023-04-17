package homemade.game.state

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.LinkCode
import homemade.game.model.CellState


interface GameState {
    val numberOfMovableBlocks: Int
    val numberOfBlocks: Int
    val spawnsDenied: Int
    val gameScore: Int
    val globalMultiplier: Int
    fun getCellState(cellCode: CellCode): CellState?
    fun getLinkBetweenCells(linkCode: LinkCode): Direction?
    fun getChainLength(linkCode: LinkCode): Int
}
