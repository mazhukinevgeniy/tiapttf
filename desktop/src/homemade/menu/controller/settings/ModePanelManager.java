package homemade.menu.controller.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.HandlerButtons;
import homemade.menu.model.settings.Presets;
import homemade.menu.view.settings.ModePanel;

import java.util.EnumMap;
import java.util.Map;

public class ModePanelManager implements HandlerButtons {

    private SettingsManager mainManager;
    private Presets.Mode panelCode;
    private ModePanel panel;

    public ModePanelManager(SettingsManager mainManager, Presets.Mode panelCode, String name) {
        this.mainManager = mainManager;
        this.panelCode = panelCode;
        ButtonActionListener actionListener = new ButtonActionListener(this);

        Map<Presets.Difficulty, String> buttons = createButtonsMap();
        panel = new ModePanel(name, buttons, actionListener);
    }

    private Map<Presets.Difficulty, String> createButtonsMap() {
        Map<Presets.Difficulty, String> buttons = new EnumMap<>(Presets.Difficulty.class);
        buttons.put(Presets.Difficulty.EASY, "Easy");
        buttons.put(Presets.Difficulty.MEDIUM, "Medium");
        buttons.put(Presets.Difficulty.HARD, "Hard");

        return buttons;
    }

    @Override
    public void handleButtonClick(int codeButton) {
        Presets.Difficulty difficultyCode = Presets.Difficulty.values()[codeButton];
        mainManager.switchToMode(panelCode, difficultyCode);
    }

    public ModePanel getModePanel() {
        return panel;
    }
}
