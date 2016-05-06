package homemade.menu.controller.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.HandlerButtons;
import homemade.menu.controller.MenuManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.MenuPanel;
import homemade.menu.view.settings.SettingsMenu;
import javafx.util.Pair;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

public class SettingsManager implements HandlerButtons
{

    public enum CodeButton
    {
        RESET,
        BACK_TO_MENU,
        APPLY
    }

    private MenuManager manager;

    private Settings settings;
    private SettingsMenu settingsMenu;

    private Map<String, Pair<Type, ?>> parameters;

    public SettingsManager(MenuManager manager, Settings settings)
    {
        this.manager = manager;
        this.settings = settings;
        ButtonActionListener actionListener = new ButtonActionListener(this);

        parameters = settings.getAllParameters();
        Map<CodeButton, String> buttons = createButtonsMap();
        settingsMenu = new SettingsMenu(parameters, buttons, actionListener);
    }

    private Map<CodeButton, String> createButtonsMap()
    {
        Map<CodeButton, String> buttons = new EnumMap<>(CodeButton.class);
        buttons.put(CodeButton.RESET, "Reset");
        buttons.put(CodeButton.BACK_TO_MENU, "Back to menu");
        buttons.put(CodeButton.APPLY, "Apply");

        return  buttons;
    }

    public MenuPanel getSettingsMenu()
    {
        return this.settingsMenu;
    }

    @Override
    public void handleButtonClick(int code)
    {
        CodeButton codeButton = CodeButton.values()[code];

        if(codeButton == CodeButton.APPLY)
        {
            settings.setParameters(settingsMenu.getParameters());
            updateSettingsMenu();
        }
        else if (codeButton == CodeButton.BACK_TO_MENU)
        {
            manager.switchToMenu(MenuManager.MenuCode.MAIN_MENU);
        }
        else if (codeButton == CodeButton.RESET)
        {
            settings.setDefaultSettings();
            updateSettingsMenu();
        }
    }

    private void updateSettingsMenu()
    {
        parameters = settings.getAllParameters();
        settingsMenu.updateMenu(parameters);
    }
}
