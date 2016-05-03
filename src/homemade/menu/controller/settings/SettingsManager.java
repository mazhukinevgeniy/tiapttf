package homemade.menu.controller.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.HandlerButtons;
import homemade.menu.controller.MenuManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.MenuPanel;
import homemade.menu.view.settings.SettingsMenu;
import javafx.util.Pair;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SettingsManager implements HandlerButtons
{
    private final class NameButton
    {
        private static final String RESET = "Reset";
        private static final String BACK_TO_MENU = "Back to menu";
        private static final String APPLY = "Apply";
    }

    private static final class CodeButton
    {
        private static final Integer RESET = 0;
        private static final Integer BACK_TO_MENU = 1;
        private static final Integer APPLY = 2;
    }

    private MenuManager manager;

    private Settings settings;
    private ButtonActionListener actionListener;
    private SettingsMenu settingsMenu;

    private Map<String, Pair<Type, ?>> parameters;

    private SettingsManager() {}

    public SettingsManager(MenuManager manager, Settings settings)
    {
        this.manager = manager;
        this.settings = settings;
        actionListener = new ButtonActionListener<>(this);

        parameters = settings.getAllParameters();
        Map<Integer, String> buttons = createButtonsMap();
        settingsMenu = new SettingsMenu(parameters, buttons, actionListener);
    }

    private Map<Integer, String> createButtonsMap()
    {
        Map<Integer, String> buttons = new HashMap<>();
        buttons.put(CodeButton.RESET, NameButton.RESET);
        buttons.put(CodeButton.BACK_TO_MENU, NameButton.BACK_TO_MENU);
        buttons.put(CodeButton.APPLY, NameButton.APPLY);

        return  buttons;
    }

    public MenuPanel getSettingsMenu()
    {
        return this.settingsMenu;
    }

    @Override
    public void handleButtonClick(int codeButton)
    {
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
