package homemade.game.state

import homemade.game.state.immutable.GameStateEncoder
import homemade.game.state.impl.SpawnPeriod

class MutableGameState(
        fieldState: MutableFieldState,
        selectionState: MutableSelectionState,
        configState: MutableConfigState
) : GameState() {
    private var isDirtyField = false
    private var isDirtySelection = false
    private var lastSnapshot: GameState = createImmutable()

    override val fieldState: FieldState
        get() = mutableFieldState
    override val configState: ConfigState
        get() = mutableConfigState
    override val selectionState: SelectionState
        get() = mutableSelectionState

    private val mutableFieldState: MutableFieldState = fieldState
    private val mutableSelectionState: MutableSelectionState = selectionState
    private val mutableConfigState: MutableConfigState = configState

    private val spawnPeriod = SpawnPeriod.newFastStart(fieldState.structure, configState.settings)
    override fun currentSpawnPeriod(): Int {
        return spawnPeriod.getSpawnPeriod(fieldState, configState)
    }

    fun changeField(): MutableFieldState {
        isDirtyField = true
        return mutableFieldState
    }

    fun changeSelection(): MutableSelectionState {
        isDirtySelection = true
        return mutableSelectionState
    }

    fun changeConfig(): MutableConfigState {
        return mutableConfigState
    }

    //todo handle initial spawns/start somehow
    fun createImmutable(): GameState {
        val field = if (isDirtyField) {
            isDirtyField = false
            GameStateEncoder().encode(fieldState)
        } else lastSnapshot.fieldState
        val selectionState = if (isDirtySelection) {
            isDirtySelection = false
            mutableSelectionState.copySelectionState()
        } else lastSnapshot.selectionState
        val config = mutableConfigState.copyConfigState()

        lastSnapshot = object : GameState() {
            val currentSpawnPeriod = this@MutableGameState.currentSpawnPeriod()
            override val fieldState: FieldState = field
            override val selectionState: SelectionState = selectionState
            override val configState: ConfigState = config

            override fun currentSpawnPeriod(): Int {
                return currentSpawnPeriod
            }
        }
        return lastSnapshot
    }
}
