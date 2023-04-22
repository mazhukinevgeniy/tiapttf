package homemade.game.model

import homemade.game.model.JsonEncoder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JsonEncoderTest {

    @Test
    fun encode() {
    }

    @Test
    fun decode() {
    }

    @Test
    fun stability() {
        val begin = """{
        |    "width":3,
        |    "height":3,
        |    "spawnPeriod":100,
        |    "settings":{"gameMode":"TURN_BASED","minCombo":5,"spawn":3,"period":200,"maxBlockValue":9},
        |    "cellStates":[
        |        {"movable":true,"cellValue":7,"effect":"UNDEFINED_COMBO_EFFECT","cellType":"OCCUPIED"},
        |        {"cellType":"MARKED_FOR_SPAWN"},
        |        {"movable":false,"cellValue":1,"effect":"UNDEFINED_COMBO_EFFECT","cellType":"OCCUPIED"},
        |        {"movable":false,"cellValue":6,"effect":"UNDEFINED_COMBO_EFFECT","cellType":"OCCUPIED"},
        |        {"movable":true,"cellValue":5,"effect":"UNDEFINED_COMBO_EFFECT","cellType":"OCCUPIED"},
        |        {"cellType":"MARKED_FOR_SPAWN"},
        |        {"movable":true,"cellValue":3,"effect":"UNDEFINED_COMBO_EFFECT","cellType":"OCCUPIED"},
        |        {"movable":true,"cellValue":9,"effect":"UNDEFINED_COMBO_EFFECT","cellType":"OCCUPIED"},
        |        {"cellType":"MARKED_FOR_SPAWN"}
        |    ],
        |    "denies":0,
        |    "score":0,
        |    "multiplier":1
        }""".trimMargin().filter { it !in setOf(' ', '\n') }

        val encoder = JsonEncoder()

        val gs1 = encoder.decode(begin)
        val s2 = encoder.encode(gs1)
        val gs2 = encoder.decode(s2)
        val s3 = encoder.encode(gs2)

        assertEquals(begin, s3)
    }
}
