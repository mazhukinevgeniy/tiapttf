package homemade.game.state

abstract class GameState {
    abstract val fieldState: FieldState

    abstract val selectionState: SelectionState

    abstract val configState: ConfigState

    abstract fun currentSpawnPeriod(): Int
}
