package homemade.game.state

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.LinkCode
import homemade.game.model.CellState

class MutableGameState : GameState {
    override val gameScore: Int
        get() = TODO("Not yet implemented")
    override val numberOfMovableBlocks: Int
        get() = TODO("Not yet implemented")
    override val numberOfBlocks: Int
        get() = TODO("Not yet implemented")
    override val spawnsDenied: Int
        get() = TODO("Not yet implemented")
    override val globalMultiplier: Int
        get() = TODO("Not yet implemented")

    override fun getCellState(cellCode: CellCode): CellState? {
        TODO("Not yet implemented")
    }

    override fun getLinkBetweenCells(linkCode: LinkCode): Direction? {
        TODO("Not yet implemented")
    }

    override fun getChainLength(linkCode: LinkCode): Int {
        TODO("Not yet implemented")
    }
}
