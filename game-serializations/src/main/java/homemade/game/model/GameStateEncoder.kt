package homemade.game.model

import homemade.game.state.GameState

interface GameStateEncoder {
    fun encode(state: GameState): String

    fun decode(input: String): GameState
}
