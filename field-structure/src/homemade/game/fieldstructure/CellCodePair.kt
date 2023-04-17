package homemade.game.fieldstructure

class CellCodePair(link: LinkCode) {
    var lower: CellCode?
        private set
    var higher: CellCode?
        private set

    init {
        lower = link.lower
        higher = link.higher
    }

    fun move(direction: Direction) {
        if (isValid) {
            lower = lower!!.neighbour(direction)
            higher = higher!!.neighbour(direction)
        }
    }

    val isValid: Boolean
        get() = lower != null && higher != null
}
