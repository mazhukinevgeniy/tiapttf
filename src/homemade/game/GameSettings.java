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
        minCombo = settings.get(Settings.Name.comboLength);
        spawn = settings.get(Settings.Name.simultaneousSpawn);
        period = settings.get(Settings.Name.spawnPeriod);
        maxBlockValue = settings.get(Settings.Name.maxBlockValue);

        gameMode = settings.get(Settings.Name.isRealTime) ? GameMode.REAL_TIME : GameMode.TURN_BASED;
    }
}
