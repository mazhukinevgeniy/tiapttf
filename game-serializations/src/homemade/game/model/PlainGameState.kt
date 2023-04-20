package homemade.game.model

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.FieldStructure
import homemade.game.fieldstructure.LinkCode
import homemade.game.state.ConfigState
import homemade.game.state.FieldState
import homemade.game.state.GameState
import homemade.game.state.SelectionState

internal data class PlainGameState(
        val width: Int,
        val height: Int
) : GameState() {

    @Transient
    override val fieldState = object : FieldState() {
        override val structure = FieldStructure(width, height)

        override fun getCellState(cellCode: CellCode): CellState {
            TODO("Not yet implemented")
        }

        override fun getLinkBetweenCells(linkCode: LinkCode): Direction? {
            TODO("Not yet implemented")
        }

        override fun getChainLength(linkCode: LinkCode): Int {
            TODO("Not yet implemented")
        }
    }

    @Transient
    override val selectionState = object : SelectionState {
        override val selection: CellCode?
            get() = TODO("Not yet implemented")
        override val cellsToMove: Set<CellCode>
            get() = TODO("Not yet implemented")

        override fun isSelected(cellCode: CellCode?): Boolean {
            TODO("Not yet implemented")
        }

        override fun canMoveTo(cellCode: CellCode?): Boolean {
            TODO("Not yet implemented")
        }
    }

    @Transient
    override val configState = object : ConfigState(GameSettings()) {
        override val spawnsDenied: Int
            get() = TODO("Not yet implemented")
        override val gameScore: Int
            get() = TODO("Not yet implemented")
        override val globalMultiplier: Int
            get() = TODO("Not yet implemented")
    }

    constructor(source: GameState) : this(
            source.fieldState.structure.width,
            source.fieldState.structure.height
    )

    override fun currentSpawnPeriod(): Int {
        TODO("Not yet implemented")
    }
}
