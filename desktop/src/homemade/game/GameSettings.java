package homemade.game;

import homemade.menu.model.settings.Settings;

/**
 * Snapshot of settings which were used to create a game
 */
public class GameSettings {
    public enum GameMode {
        TURN_BASED, REAL_TIME
    }

    public final GameMode gameMode;

    public final int minCombo;
    public final int spawn;
    public final int period;
    public final int maxBlockValue;

    public GameSettings(Settings settings) {
        minCombo = settings.get(Settings.Code.COMBO_LENGTH);
        spawn = settings.get(Settings.Code.SIMULTANEOUS_SPAWN);
        period = settings.get(Settings.Code.SPAWN_PERIOD);
        maxBlockValue = settings.get(Settings.Code.MAX_BLOCK_VALUE);

        gameMode = settings.get(Settings.Code.IS_REALTIME) ? GameMode.REAL_TIME : GameMode.TURN_BASED;
    }
}
