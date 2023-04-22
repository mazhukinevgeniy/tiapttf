package homemade.game.fieldstructure

class LinkCode internal constructor(val lowerToHigherDirection: Direction,
                                    @JvmField val lower: CellCode,
                                    @JvmField val higher: CellCode,
                                    private val code: Int) {
    override fun hashCode(): Int {
        return code
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }
}
