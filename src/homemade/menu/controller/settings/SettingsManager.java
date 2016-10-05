package homemade.menu.controller.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.HandlerButtons;
import homemade.menu.controller.MenuManager;
import homemade.menu.model.settings.Parameter;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.MenuPanel;
import homemade.menu.view.settings.SettingsMenu;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SettingsManager implements HandlerButtons
{
    public enum CodeButton
    {
        TURN_BASED_EASY,
        TURN_BASED_MEDIUM,
        TURN_BASED_HARD,
        REALTIME_EASY,
        REALTIME_MEDIUM,
        REALTIME_HARD,
        BACK_TO_MENU,
        CUSTOM
    }

    private MenuManager mainManager;

    private Settings settings;
    private SettingsMenu settingsMenu;

    private CustomManager customManager;

    private List<Parameter<?>> parameters;

    public SettingsManager(MenuManager mainManager, Settings settings)
    {
        this.mainManager = mainManager;
        this.settings = settings;
        ButtonActionListener actionListener = new ButtonActionListener(this);

        parameters = settings.getAllParameters();
        customManager = new CustomManager(this, parameters);

        Map<CodeButton, String> buttons = createButtonsMap();
        settingsMenu = new SettingsMenu(parameters, buttons, actionListener);
    }

    private Map<CodeButton, String> createButtonsMap()
    {
        Map<CodeButton, String> buttons = new EnumMap<>(CodeButton.class);
        buttons.put(CodeButton.TURN_BASED_EASY, "Easy");
        buttons.put(CodeButton.TURN_BASED_MEDIUM, "Medium");
        buttons.put(CodeButton.TURN_BASED_HARD, "Hard");
        buttons.put(CodeButton.REALTIME_EASY, "Easy");
        buttons.put(CodeButton.REALTIME_MEDIUM, "Medium");
        buttons.put(CodeButton.REALTIME_HARD, "Hard");
        buttons.put(CodeButton.BACK_TO_MENU, "Back to menu");
        buttons.put(CodeButton.CUSTOM, "Custom");

        return  buttons;
    }

    public MenuPanel getSettingsMenu()
    {
        return this.settingsMenu;
    }

    public MenuPanel getCustomMenu()
    {
        return customManager.getCustomMenu();
    }

    @Override
    public void handleButtonClick(int code)
    {
        CodeButton codeButton = CodeButton.values()[code];

        if (codeButton == CodeButton.BACK_TO_MENU)
        {
            mainManager.switchToMenu(MenuManager.MenuCode.MAIN_MENU);
        }
        else if (codeButton == CodeButton.CUSTOM)
        {
            mainManager.switchToMenu(MenuManager.MenuCode.CUSTOM);
        }
    }

    private void updateSettingsMenu()
    {
        parameters = settings.getAllParameters();
        settingsMenu.updateMenu(parameters);
    }
}
