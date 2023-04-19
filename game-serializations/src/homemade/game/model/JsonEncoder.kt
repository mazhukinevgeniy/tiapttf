package homemade.game.model

import com.google.gson.Gson
import homemade.game.state.GameState

class JsonEncoder {
    private val gson = Gson()

    fun encode(state: GameState): String {
        return gson.toJson(PlainGameState(state))
    }

    fun decode(input: String): GameState {
        return gson.fromJson(input, PlainGameState::class.java)
    }
}
