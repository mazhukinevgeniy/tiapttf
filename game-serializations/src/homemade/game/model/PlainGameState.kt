package homemade.game.model

import homemade.game.state.ConfigState
import homemade.game.state.FieldState
import homemade.game.state.GameState
import homemade.game.state.SelectionState

internal data class PlainGameState(
        val width: Int,
        val height: Int
) : GameState() {

    constructor(source: GameState) : this(source.fieldState.structure.width, source.fieldState.structure.height)

    override val fieldState: FieldState
        get() = TODO("Not yet implemented")
    override val selectionState: SelectionState
        get() = TODO("Not yet implemented")
    override val configState: ConfigState
        get() = TODO("Not yet implemented")

    override fun currentSpawnPeriod(): Int {
        TODO("Not yet implemented")
    }
}
