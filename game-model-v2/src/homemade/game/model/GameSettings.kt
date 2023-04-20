package homemade.game.model

/**
 * Snapshot of settings which were used to create a game
 */
data class GameSettings(
        val gameMode: GameMode = GameMode.TURN_BASED,
        val minCombo: Int = 5,
        val spawn: Int = 4,
        val period: Int = 500,
        val maxBlockValue: Int = 9
) {
    enum class GameMode {
        TURN_BASED,
        REAL_TIME
    }

    override fun toString(): String {
        return "sp${spawn}c${minCombo}per${period}max${maxBlockValue}${if (gameMode == GameMode.TURN_BASED) "tb" else "rt"}"
    }
}
