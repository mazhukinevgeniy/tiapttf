package homemade.game;

import homemade.menu.model.settings.Settings;

/**
 * Snapshot of settings which were used to create a game
 */
public class GameSettings
{
    public enum GameMode
    {
        TURN_BASED, REAL_TIME
    }

    private GameMode mode;

    private int combo;
    private int period;

    public GameSettings(Settings settings)
    {
        combo = settings.get(Settings.Name.comboLength);
        period = settings.get(Settings.Name.spawnPeriod);

        mode = settings.get(Settings.Name.isRealTime) ? GameMode.REAL_TIME : GameMode.TURN_BASED;
    }

    public GameMode gameMode()
    {
        return mode;
    }

    public int minCombo()
    {
        return combo;
    }

    public int maxPeriod()
    {
        return period;
    }
}
