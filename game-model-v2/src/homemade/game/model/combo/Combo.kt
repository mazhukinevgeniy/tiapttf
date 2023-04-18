package homemade.game.model.combo

import homemade.game.fieldstructure.CellCode

class Combo(cells: Set<CellCode>, tier: Int) {
    private val cells: Set<CellCode>

    @JvmField
    val tier: Int = tier

    init {
        check(tier > 0)
        this.cells = cells
    }

    fun toSet(): Set<CellCode> {
        return HashSet(cells)
    }
}
