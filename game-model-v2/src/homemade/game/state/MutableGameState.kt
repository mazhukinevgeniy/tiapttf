package homemade.game.state

import homemade.game.state.immutable.GameStateEncoder
import homemade.game.state.impl.SpawnPeriod

class MutableGameState(
        override var fieldState: MutableFieldState,
        override var selectionState: MutableSelectionState,
        override var configState: MutableConfigState
) : GameState() {
    var isDirtyField = false
    var isDirtySelection = false
    private var lastSnapshot: GameState = createImmutable()

    private val spawnPeriod = SpawnPeriod.newFastStart(fieldState.structure, configState.settings)
    override fun currentSpawnPeriod(): Int {
        return spawnPeriod.getSpawnPeriod(fieldState, configState)
    }

    //todo handle initial spawns/start somehow
    fun createImmutable(): GameState {
        val field = if (isDirtyField) {
            isDirtyField = false
            GameStateEncoder().encode(fieldState)
        } else lastSnapshot.fieldState
        val selectionState = if (isDirtySelection) {
            isDirtySelection = false
            selectionState.copySelectionState()
        } else lastSnapshot.selectionState
        val config = configState.copyConfigState()

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
