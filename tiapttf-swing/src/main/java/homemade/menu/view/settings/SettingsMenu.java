package homemade.menu.view.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.settings.SettingsManager.CodeButton;
import homemade.menu.view.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SettingsMenu extends MenuPanel {
    public SettingsMenu(Map<CodeButton, String> buttons,
                        ButtonActionListener actionListener, List<ModePanel> panels) {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        initializeModePanels(panels);
        initializeButtonPanel(buttons, actionListener);
    }

    private void initializeModePanels(List<ModePanel> panels) {
        for (ModePanel panel : panels) {
            add(panel);
        }
    }

    private void initializeButtonPanel(Map<CodeButton, String> buttons, ButtonActionListener actionListener) {
        JPanel panel = new JPanel(new FlowLayout());

        Set<CodeButton> keys = buttons.keySet();
        for (CodeButton key : keys) {
            String nameButton = buttons.get(key);
            JButton button = SettingsButtonFactory.createButton(nameButton, actionListener);
            button.setActionCommand(String.valueOf(key.ordinal()));
            panel.add(button);
        }
        add(panel);
    }
}
