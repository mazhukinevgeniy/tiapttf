package homemade.game.model

/**
 * Snapshot of settings which were used to create a game
 */
class GameSettings(val gameMode: GameMode, val minCombo: Int, val spawn: Int, val period: Int, val maxBlockValue: Int) {
    enum class GameMode {
        TURN_BASED,
        REAL_TIME
    }

    override fun toString(): String {
        return "sp${spawn}c${minCombo}per${period}max${maxBlockValue}${if (gameMode == GameMode.TURN_BASED) "tb" else "rt"}"
    }
}
