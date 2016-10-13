package homemade.menu.controller.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.HandlerButtons;
import homemade.menu.model.settings.Modes;
import homemade.menu.view.settings.ModePanel;

import java.util.EnumMap;
import java.util.Map;

public class ModePanelManager implements HandlerButtons
{

    private SettingsManager mainManager;
    private Modes.GroupCode panelCode;
    private ModePanel panel;

    public ModePanelManager(SettingsManager mainManager, Modes.GroupCode panelCode, String name)
    {
        this.mainManager = mainManager;
        this.panelCode = panelCode;
        ButtonActionListener actionListener = new ButtonActionListener(this);

        Map<Modes.ModeCode, String> buttons = createButtonsMap();
        panel = new ModePanel(name, buttons, actionListener);
    }

    private Map<Modes.ModeCode, String> createButtonsMap()
    {
        Map<Modes.ModeCode, String> buttons = new EnumMap<>(Modes.ModeCode.class);
        buttons.put(Modes.ModeCode.EASY, "Easy");
        buttons.put(Modes.ModeCode.MEDIUM, "Medium");
        buttons.put(Modes.ModeCode.HARD, "Hard");

        return buttons;
    }

    @Override
    public void handleButtonClick(int codeButton)
    {
        Modes.ModeCode modeCode = Modes.ModeCode.values()[codeButton];
        mainManager.switchToMode(panelCode, modeCode, null);
    }

    public ModePanel getModePanel()
    {
        return panel;
    }
}
