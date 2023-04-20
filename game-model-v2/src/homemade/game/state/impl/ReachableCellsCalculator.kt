package homemade.game.state.impl

import homemade.game.fieldstructure.CellCode
import homemade.game.state.FieldState

class ReachableCellsCalculator {

    fun buildReachableCellSet(field: FieldState, start: CellCode?): HashSet<CellCode> {
        if (start == null) {
            return HashSet()
        }
        val visited = hashSetOf(start)
        val accessible = HashSet<CellCode>()
        val toCheck = start.vicinity.toMutableList()
        while (toCheck.isNotEmpty()) {
            val next = toCheck.removeAt(toCheck.size - 1)
            if (next !in visited) {
                visited.add(next)
                if (field.getCellState(next).isFreeForMove) {
                    accessible.add(next)
                    for (item in next.vicinity) {
                        if (item !in visited) {
                            toCheck.add(item)
                        }
                    }
                }
            }
        }
        return accessible
    }
}
