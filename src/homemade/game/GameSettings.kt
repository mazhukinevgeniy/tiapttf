package homemade.game

import homemade.menu.model.settings.Settings

/**
 * Snapshot of settings which were used to create a game
 */
class GameSettings(settings: Settings) {
    enum class GameMode {
        TURN_BASED,
        REAL_TIME
    }

    val gameMode: GameMode = if (settings.get(Settings.Code.IS_REALTIME)) GameMode.REAL_TIME else GameMode.TURN_BASED

    val minCombo: Int = settings.get(Settings.Code.COMBO_LENGTH)

    val spawn: Int = settings.get(Settings.Code.SIMULTANEOUS_SPAWN)

    val period: Int = settings.get(Settings.Code.SPAWN_PERIOD)

    val maxBlockValue: Int = settings.get(Settings.Code.MAX_BLOCK_VALUE)

    override fun toString(): String {
        return "sp${spawn}c${minCombo}per${period}max${maxBlockValue}${if (gameMode == GameMode.TURN_BASED) "tb" else "rt"}"
    }
}
