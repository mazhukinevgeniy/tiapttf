package homemade.game.model.combo

import homemade.game.fieldstructure.CellCode

/**
 * Represents a set of combos, created "simultaneously".
 */
class ComboPack {
    private val combos: ArrayList<Combo> = ArrayList()
    private var tier = 0
    private var bonusMultiplier = 0

    fun addMultiplier(value: Int) {
        bonusMultiplier += value
    }

    fun add(combo: Combo) {
        combos.add(combo)
        tier += combo.tier
    }

    fun append(anotherPack: ComboPack) {
        combos.addAll(anotherPack.combos)
        tier += anotherPack.tier
        bonusMultiplier += anotherPack.bonusMultiplier
    }

    fun cellSet(): Set<CellCode> {
        val cells: MutableSet<CellCode> = HashSet()
        for (combo in combos) cells.addAll(combo.toSet())
        return cells
    }

    fun comboIterator(): Iterator<Combo> {
        return combos.iterator()
    }

    fun numberOfCombos(): Int {
        return combos.size
    }

    fun packTier(): Int {
        return tier
    }

    fun multiplierIncrease(): Int {
        return tier + bonusMultiplier
    }
}
