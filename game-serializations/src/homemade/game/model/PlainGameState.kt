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
        val selection: Coordinates?,
        val denies: Int,
        val score: Int,
        val multiplier: Int
) : GameState() {

    @Transient
    override lateinit var fieldState: FieldState
        private set

    @Transient
    override lateinit var selectionState: SelectionState
        private set

    @Transient
    override lateinit var configState: ConfigState
        private set

    init {
        buildTransient()
    }

    internal fun buildTransient() {
        fieldState = PlainFieldState.buildConsistent(
                FieldStructure(width, height),
                cellStates,
                settings.maxBlockValue
        )
        selectionState = MutableSelectionState(
                selection?.let { fieldState.structure.getCellCode(it.x, it.y) },
                ReachableCellsCalculator().buildReachableCellSet(
                        fieldState, selection?.let { fieldState.structure.getCellCode(it.x, it.y) }
                )
        )
        configState = object : ConfigState(settings) {
            override val spawnsDenied: Int
                get() = denies
            override val gameScore: Int
                get() = score
            override val globalMultiplier: Int
                get() = multiplier
        }
    }

    constructor(source: GameState) : this(
            width = source.fieldState.structure.width,
            height = source.fieldState.structure.height,
            spawnPeriod = source.currentSpawnPeriod(),
            settings = source.configState.settings,
            cellStates = PlainFieldState(source.fieldState).cellStates,
            selection = source.selectionState.selection?.let { Coordinates(it.x, it.y) },
            denies = source.configState.spawnsDenied,
            score = source.configState.gameScore,
            multiplier = source.configState.globalMultiplier
    )
    //TODO this loses stored effects, upsettingly. do i care? not yet

    override fun currentSpawnPeriod(): Int {
        return spawnPeriod
    }
}
