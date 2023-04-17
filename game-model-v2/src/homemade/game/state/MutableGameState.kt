package homemade.game.state

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.FieldStructure
import homemade.game.fieldstructure.LinkCode
import homemade.game.model.CellState

class MutableGameState(structure: FieldStructure) : GameState(structure) {

    override fun getCellState(cellCode: CellCode): CellState? {
        //TODO("Not yet implemented")
        return null
    }

    override fun getLinkBetweenCells(linkCode: LinkCode): Direction? {
        //TODO("Not yet implemented")
        return null
    }

    override fun getChainLength(linkCode: LinkCode): Int {
        //TODO("Not yet implemented")
        return 0
    }
}
