package homemade.game.state

import homemade.game.model.combo.ComboEffect
import homemade.game.state.immutable.PlainFieldState
import homemade.game.state.impl.SpawnPeriod

class MutableGameState(
        fieldState: MutableFieldState,
        selectionState: MutableSelectionState,
        configState: MutableConfigState
) : GameState() {
    // scenario: user created some nice combos, and earned a lot of good combo effects,
    // but there aren't enough blocks to mark with them. so the effects persist until the time comes
    val storedEffects = ArrayList<ComboEffect>()

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

    private var isDirtyField = true
    private var isDirtySelection = true
    private var lastSnapshot: GameState = createImmutable()

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
            PlainFieldState(fieldState)
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
