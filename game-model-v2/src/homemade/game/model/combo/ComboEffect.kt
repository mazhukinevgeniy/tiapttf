package homemade.game.model.combo

enum class ComboEffect(@JvmField val price: Int) {
    //care: all prices must be different
    UNDEFINED_COMBO_EFFECT(Int.MAX_VALUE / 10),
    EXTRA_MULTIPLIER(1),
    JUST_EXTRA_TIER(2),
    EXPLOSION(4);

    fun tierBonus(): Int {
        return if (this == EXTRA_MULTIPLIER) 0 else 1
    }

    fun multiplierBonus(): Int {
        return if (this == EXTRA_MULTIPLIER) 1 else 0
    }

    companion object {
        @JvmStatic
        val comparator: Comparator<ComboEffect>
            /**
             * sort from the most expensive to the least expensive effects
             */
            get() = Comparator { o1, o2 ->
                if (o1 == null || o2 == null) {
                    throw NullPointerException()
                }
                o2.price - o1.price
            }
    }
}
