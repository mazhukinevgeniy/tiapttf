package homemade.game.model

import com.google.gson.*
import homemade.game.model.cellstates.BlockState
import homemade.game.model.cellstates.SimpleState
import homemade.game.state.GameState
import java.lang.reflect.Type


class JsonEncoder {
    private val gson = GsonBuilder()
            .registerTypeAdapter(CellState::class.java, object : JsonDeserializer<CellState> {
                override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): CellState {
                    val jsonObject = json.asJsonObject
                    val cellType = (jsonObject["cellType"] as JsonPrimitive).asString
                    return when (Cell.valueOf(cellType)) {
                        Cell.OCCUPIED -> context.deserialize(jsonObject, BlockState::class.java)
                        in SimpleState.allStates -> context.deserialize(jsonObject, SimpleState::class.java)
                        else -> throw JsonParseException("invalid celltype $cellType")
                    }
                }
            })
            .registerTypeAdapter(GameState::class.java, object : JsonDeserializer<GameState> {
                override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): GameState {
                    val result: PlainGameState = context.deserialize(json.asJsonObject, PlainGameState::class.java)
                    result.buildTransient()
                    return result
                }
            })
            .create()

    fun encode(state: GameState): String {
        return gson.toJson(PlainGameState(state))
    }

    fun decode(input: String): GameState {
        return gson.fromJson(input, GameState::class.java)
    }
}
