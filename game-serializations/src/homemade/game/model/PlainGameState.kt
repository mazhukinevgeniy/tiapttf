package homemade.game.model

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.FieldStructure
import homemade.game.state.ConfigState
import homemade.game.state.FieldState
import homemade.game.state.GameState
import homemade.game.state.SelectionState
import homemade.game.state.immutable.PlainFieldState

internal data class PlainGameState(
        val width: Int,
        val height: Int,
        val spawnPeriod: Int,
        val settings: GameSettings,
        val cellStates: List<CellState>
) : GameState() {

    @Transient
    override val fieldState: FieldState = PlainFieldState.buildConsistent(
            FieldStructure(width, height),
            cellStates,
            settings.maxBlockValue
    )

    @Transient
    override val selectionState = object : SelectionState {
        override val selection: CellCode?
            get() = TODO("Not yet implemented")
        override val cellsToMove: Set<CellCode>
            get() = TODO("Not yet implemented")
    }

    @Transient
    override val configState = object : ConfigState(settings) {
        override val spawnsDenied: Int
            get() = TODO("Not yet implemented")
        override val gameScore: Int
            get() = TODO("Not yet implemented")
        override val globalMultiplier: Int
            get() = TODO("Not yet implemented")
    }

    constructor(source: GameState) : this(
            source, PlainFieldState(source.fieldState)
    )

    private constructor(source: GameState, intermediateField: PlainFieldState) : this(
            width = source.fieldState.structure.width,
            height = source.fieldState.structure.height,
            spawnPeriod = source.currentSpawnPeriod(),
            settings = source.configState.settings,
            cellStates = intermediateField.cellStates
    )

    override fun currentSpawnPeriod(): Int {
        return spawnPeriod
    }
}
