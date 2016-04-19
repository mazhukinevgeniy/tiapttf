package homemade.menu.view.settings;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marid on 18.04.2016.
 */
class NumberParameterFactory
{
    private static final Font FONT_TEXT_FIELD = new Font("Verdana", Font.PLAIN, 13);
    private static final int COLUMNS_TEXT_FIELD = 5;

    public static JPanel createParameterPanel(final String nameParameter, final String value)
    {
        JPanel panel = new JPanel(new FlowLayout());

        JLabel label = new JLabel(nameParameter);
        JTextField textField = new JTextField(value, COLUMNS_TEXT_FIELD);
        textField.setFont(FONT_TEXT_FIELD);

        panel.add(label);
        panel.add(textField);

        return panel;
    }
}
