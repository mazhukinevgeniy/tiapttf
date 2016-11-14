package homemade.menu.controller.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.HandlerButtons;
import homemade.menu.controller.MenuManager;
import homemade.menu.model.settings.Parameter;
import homemade.menu.model.settings.Presets;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.MenuPanel;
import homemade.menu.view.settings.ModePanel;
import homemade.menu.view.settings.SettingsMenu;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SettingsManager implements HandlerButtons
{
    public enum CodeButton
    {
        BACK_TO_MENU,
        CUSTOM
    }

    private MenuManager mainManager;

    private Settings settings;
    private SettingsMenu settingsMenu;

    private CustomManager customManager;
    private ModePanelManager turnBasedManager;
    private ModePanelManager realtimeManager;

    public SettingsManager(MenuManager mainManager, Settings settings)
    {
        this.mainManager = mainManager;
        this.settings = settings;
        ButtonActionListener actionListener = new ButtonActionListener(this);

        customManager = new CustomManager(this, settings);

        turnBasedManager = new ModePanelManager(this, Presets.Mode.TURN_BASED, "Turn based");
        realtimeManager = new ModePanelManager(this, Presets.Mode.REALTIME, "Realtime");
        List<ModePanel> panels = createPanelsList();

        Map<CodeButton, String> buttons = createButtonMap();

        settingsMenu = new SettingsMenu(buttons, actionListener, panels);
    }

    private List<ModePanel> createPanelsList()
    {
        List<ModePanel> panels = new ArrayList<>();
        panels.add(turnBasedManager.getModePanel());
        panels.add(realtimeManager.getModePanel());

        return panels;
    }

    private Map<CodeButton, String> createButtonMap()
    {
        Map<CodeButton, String> buttons = new EnumMap<>(CodeButton.class);
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

    public void returnToSettingsMenu()
    {
        mainManager.switchToMenu(MenuManager.MenuCode.SETTINGS);
    }

    public void switchToMode(Presets.Mode mode, Presets.Difficulty difficulty)
    {
        settings.setPresetParameters(mode, difficulty);
    }

    public void setNewParameters(List<Parameter<?>> parameters)
    {
        settings.setParameters(parameters);
    }
}
