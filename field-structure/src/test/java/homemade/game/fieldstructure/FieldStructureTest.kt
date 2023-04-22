package homemade.game.fieldstructure

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FieldStructureTest {
    @Test
    fun getCellCodeIterator() {
        val fieldStructure = FieldStructure(3, 10)
        assertEquals(fieldStructure.fieldSize, 3 * 10)

        var code = 0
        for (cellCode in fieldStructure.cellCodeIterator) {
            assertEquals(cellCode.cellCode, code)
            code++
        }
        assertEquals(code, fieldStructure.fieldSize)
    }

    @Test
    fun getLinkCodeIterator() {
        val fieldStructure = FieldStructure(15, 20)

        var code = 0
        for (linkCode in fieldStructure.linkCodeIterator) {
            assertEquals(linkCode.hashCode(), code)
            code++
        }
        assertEquals(code, fieldStructure.numberOfLinks)
    }
}
