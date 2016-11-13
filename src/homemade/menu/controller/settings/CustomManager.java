package homemade.menu.controller.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.HandlerButtons;
import homemade.menu.model.settings.Parameter;
import homemade.menu.model.settings.Presets;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.MenuPanel;
import homemade.menu.view.settings.CustomMenu;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class CustomManager implements HandlerButtons
{
    public enum CodeButton
    {
        CANCEL,
        APPLY
    }

    private SettingsManager mainManager;

    private Settings settings;
    private CustomMenu customMenu;

    private List<Parameter<?>> parameters;

    public CustomManager(SettingsManager mainManager, Settings settings)
    {
        this.mainManager = mainManager;
        this.settings = settings;
        ButtonActionListener actionListener = new ButtonActionListener(this);

        this.parameters = settings.getAllParameters();
        Map<CodeButton, String> buttons = createButtonsMap();
        customMenu = new CustomMenu(parameters, buttons, actionListener);
    }

    private Map<CodeButton, String> createButtonsMap()
    {
        Map<CodeButton, String> buttons = new EnumMap<>(CodeButton.class);
        buttons.put(CodeButton.CANCEL, "Cancel");
        buttons.put(CodeButton.APPLY, "Apply");

        return  buttons;
    }

    public MenuPanel getCustomMenu()
    {
        return this.customMenu;
    }

    @Override
    public void handleButtonClick(int code)
    {
        CodeButton codeButton = CodeButton.values()[code];

        if (codeButton == CodeButton.CANCEL)
        {
            mainManager.returnToSettingsMenu();
        }
        else if (codeButton == CodeButton.APPLY)
        {
            mainManager.switchToMode(Presets.Mode.CUSTOM, Presets.Difficulty.CUSTOM, customMenu.getParameters());
            updateSettingsMenu();
            mainManager.returnToSettingsMenu();
        }
    }

    private void updateSettingsMenu()
    {
        parameters = settings.getAllParameters();
        customMenu.updateMenu(parameters);
    }
}
