package homemade.game.fieldstructure

import java.util.*

enum class Direction(val isHorizontal: Boolean) {
    LEFT(true),
    RIGHT(true),
    TOP(false),
    BOTTOM(false);

    val opposite: Direction?
        get() = opposites[this]

    companion object {
        private val opposites = EnumMap(mapOf(
                LEFT to RIGHT,
                RIGHT to LEFT,
                BOTTOM to TOP,
                TOP to BOTTOM
        ))
    }
}
