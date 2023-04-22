package homemade.menu.view.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.model.settings.Presets;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

public class ModePanel extends JPanel {
    public ModePanel(String name, Map<Presets.Difficulty, String> buttons,
                     ButtonActionListener actionListener) {
        super();

        initializeLabel(name);
        initializeButtonPanel(buttons, actionListener);

        setVisible(true);
    }

    private void initializeLabel(String name) {
        JLabel label = new JLabel(name);
        add(label);
    }

    private void initializeButtonPanel(Map<Presets.Difficulty, String> buttons, ButtonActionListener actionListener) {
        JPanel panel = new JPanel(new FlowLayout());

        Set<Presets.Difficulty> keys = buttons.keySet();
        for (Presets.Difficulty key : keys) {
            String nameButton = buttons.get(key);
            JButton button = SettingsButtonFactory.createButton(nameButton, actionListener);
            button.setActionCommand(String.valueOf(key.ordinal()));
            panel.add(button);
        }
        add(panel);
    }
}
