package homemade.game.model

import homemade.game.fieldstructure.FieldStructure
import homemade.game.state.*
import homemade.game.state.immutable.PlainFieldState
import homemade.game.state.impl.ReachableCellsCalculator

internal data class Coordinates(val x: Int, val y: Int)

internal data class PlainGameState(
        val width: Int,
        val height: Int,
        val spawnPeriod: Int,
        val settings: GameSettings,
        val cellStates: List<CellState>,
        val selection: Coordinates?
) : GameState() {

    @Transient
    override val fieldState: FieldState = PlainFieldState.buildConsistent(
            FieldStructure(width, height),
            cellStates,
            settings.maxBlockValue
    )

    @Transient
    override val selectionState: SelectionState = MutableSelectionState(
            selection?.let { fieldState.structure.getCellCode(it.x, it.y) },
            ReachableCellsCalculator().buildReachableCellSet(
                    fieldState, selection?.let { fieldState.structure.getCellCode(it.x, it.y) }
            )
    )

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
            cellStates = intermediateField.cellStates,
            selection = source.selectionState.selection?.let { Coordinates(it.x, it.y) }
    )

    override fun currentSpawnPeriod(): Int {
        return spawnPeriod
    }
}
