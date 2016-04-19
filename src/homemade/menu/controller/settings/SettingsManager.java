package homemade.menu.controller.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.HandlerButtons;
import homemade.menu.controller.MenuManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.Menu;
import homemade.menu.view.settings.SettingsMenu;
import javafx.util.Pair;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marid on 18.04.2016.
 */
public class SettingsManager implements HandlerButtons
{
    private final class NameButton
    {
        public static final String RESET = "Reset";
        public static final String BACK_TO_MENU = "Back to menu";
        public static final String APPLY = "Apply";
    }

    private static final class CodeButton
    {
        public static final Integer RESET = 0;
        public static final Integer BACK_TO_MENU = 1;
        public static final Integer APPLY = 2;
    }

    MenuManager manager;

    SettingsMenu settingsMenu;
    Settings settings;
    ButtonActionListener actionListener;

    Map<String, Pair<Type, ?>> parameters;

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

    public Menu getSettingsMenu()
    {
        return this.settingsMenu;
    }

    @Override
    public void handleButtonClick(int codeButton)
    {
        if(codeButton == CodeButton.APPLY)
        {
            settings.setParameters(settingsMenu.getParameters());
            parameters = settings.getAllParameters();
            settingsMenu.setParameters(parameters);
            settingsMenu.redrawUI();
        }
        else if (codeButton == CodeButton.BACK_TO_MENU)
        {
            manager.toggleToMenu(MenuManager.CodeMenu.MAIN_MENU);
        }
        else if (codeButton == CodeButton.RESET)
        {
            settings.setDefaultSettings();
            parameters = settings.getAllParameters();
            settingsMenu.setParameters(parameters);
            settingsMenu.redrawUI();
        }
    }
}
