package homemade.game;

import homemade.menu.model.settings.Settings;

/**
 * Snapshot of settings which were used to create a game
 */
public class GameSettings
{
    private int combo;

    public GameSettings(Settings settings)
    {
        combo = settings.get(Settings.Name.comboLength);
    }

    public int minCombo()
    {
        return combo;
    }
}
