package homemade.game;

import homemade.menu.model.settings.Settings;

/**
 * Snapshot of settings which were used to create a game
 */
public class GameSettings {
    public enum GameMode {
        TURN_BASED, REAL_TIME
    }

    private GameMode mode;

    private int combo;
    private int spawn;
    private int period;

    public GameSettings(Settings settings) {
        combo = settings.get(Settings.Name.comboLength);
        spawn = settings.get(Settings.Name.simultaneousSpawn);
        period = settings.get(Settings.Name.spawnPeriod);

        mode = settings.get(Settings.Name.isRealTime) ? GameMode.REAL_TIME : GameMode.TURN_BASED;
    }

    public GameMode gameMode() {
        return mode;
    }

    public int minCombo() {
        return combo;
    }

    public int maxSpawn() {
        return spawn;
    }

    public int maxPeriod() {
        return period;
    }
}
