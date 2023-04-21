package homemade.game.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PrettyEncoderTest {

    private val encoder = PrettyEncoder()
    private val initial = """1,1
            200,6,300,2
            1,5,4,9
            .........
            .6.......
            ....111..
            ..x......
            ....o..7.
            .........
            ....9876.
            2...8....
            ....7....""".filterNot { it == ' ' }

    @Test
    fun prettyPrint() {
    }

    @Test
    fun fromPrettyPrint() {
        val parsedState = encoder.decode(initial)
        val structure = parsedState.fieldState.structure

        assertEquals(parsedState.selectionState.selection?.let { listOf(it.x, it.y) }, listOf(1, 1))
        assertEquals(parsedState.fieldState.getCellState(structure.getCellCode(2, 3)).type(), Cell.DEAD_BLOCK)
        assertEquals(parsedState.fieldState.getCellState(structure.getCellCode(0, 7)).type(), Cell.OCCUPIED)
        assertEquals(parsedState.fieldState.getCellState(structure.getCellCode(0, 7)).value(), 2)

        val linkCode8to7 = structure.getLinkCode(structure.getCellCode(5, 6), structure.getCellCode(6, 6))
        assertEquals(parsedState.fieldState.getChainLength(linkCode8to7), 4)
    }

    @Test
    fun stability() {
        val state1 = encoder.decode(initial)
        val print1 = encoder.encode(state1)
        val state2 = encoder.decode(print1)
        val print2 = encoder.encode(state2)

        assertEquals(initial, print1)
        assertEquals(initial, print2)
    }
}
