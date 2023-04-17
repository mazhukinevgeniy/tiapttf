package homemade.game.state

import homemade.game.state.immutable.GameStateEncoder
import homemade.game.state.impl.SpawnPeriod

class MutableGameState(
        override var fieldState: MutableFieldState,
        override var selectionState: SelectionState,
        override var configState: ConfigState
) : GameState() {
    private var isDirtyField = false
    private var isDirtySelection = false
    private var lastSnapshot: GameState = createImmutable()

    private val spawnPeriod = SpawnPeriod.newFastStart(fieldState.structure, configState.settings)
    override fun currentSpawnPeriod(): Int {
        return spawnPeriod.getSpawnPeriod(fieldState, configState)
    }

    fun createImmutable(): GameState {
        val field = if (isDirtyField) {
            isDirtyField = false
            GameStateEncoder().encode(fieldState)
        } else lastSnapshot.fieldState
        val selectionState = if (isDirtySelection) {
            isDirtySelection = false
            selectionState.createcopy()
        } else lastSnapshot.selectionState
        val config = configState.createcopy()

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
