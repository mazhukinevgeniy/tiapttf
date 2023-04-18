package homemade.game.pipeline.operations

import homemade.game.model.combo.ComboEffect

class ComboEffectVendor {
    fun addComboEffectsForTier(effects: ArrayList<ComboEffect>, tier: Int) {
        var sumToSpend = tier
        val pricedEffects = ComboEffect.values().sortedBy { -it.price }
        var i = 0
        while (i < pricedEffects.size && sumToSpend > 0) {
            val effect = pricedEffects[i]
            val price = effect.price
            val given = sumToSpend / price
            sumToSpend -= given * price
            for (j in 0 until given) {
                effects.add(effect)
            }
            i++
        }
        effects.sortBy { -it.price }
    }
}
