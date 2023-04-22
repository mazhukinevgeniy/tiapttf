package homemade.game.pipeline.operations

import homemade.game.model.combo.ComboPack
import homemade.game.state.MutableConfigState

class GameScore {
    fun handleCombos(pack: ComboPack, state: MutableConfigState) {
        var packScore = 0
        for (combo in pack.comboIterator().asSequence()) {
            packScore += getScore(combo.tier)
        }
        if (packScore > 0) {
            packScore *= pack.packTier() * state.globalMultiplier
            state.gameScore += packScore
            state.globalMultiplier += pack.multiplierIncrease()
        }
    }

    private fun getScore(tier: Int): Int {
        val baseScore = 5
        return baseScore * tier * tier
    }
}
