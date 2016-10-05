package homemade.menu.controller.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.HandlerButtons;
import homemade.menu.model.settings.Parameter;
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
    private List<Parameter<?>> parameters;

    private CustomMenu customMenu;

    public CustomManager(SettingsManager mainManager, List<Parameter<?>> parameters)
    {
        this.mainManager = mainManager;
        this.parameters = parameters;
        ButtonActionListener actionListener = new ButtonActionListener(this);

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
    public void handleButtonClick(int codeButton)
    {

    }
}
