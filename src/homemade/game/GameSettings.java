package homemade.game;

import homemade.menu.model.settings.Settings;

/**
 * Snapshot of settings which were used to create a game
 */
public class GameSettings
{
    private int combo;
    private int period;

    public GameSettings(Settings settings)
    {
        combo = settings.get(Settings.Name.comboLength);
        period = settings.get(Settings.Name.spawnPeriod);
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
