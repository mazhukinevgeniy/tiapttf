package homemade.menu.view.settings;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marid on 18.04.2016.
 */
class NumberParameter implements ParameterFactory<JPanel, Number>
{
    private static class Index
    {
        public static final int LABEL = 0;
        public static final int TEXT_FIELD = 1;
    }

    private static final Font FONT_TEXT_FIELD = new Font("Verdana", Font.PLAIN, 13);
    private static final int COLUMNS_TEXT_FIELD = 5;

    public NumberParameter() {}

    @Override
    public JPanel create(final String nameParameter, final Number value)
    {
        JPanel panel = new JPanel(new FlowLayout());

        JLabel label = new JLabel(nameParameter);
        String strValue = String.valueOf(value);
        JTextField textField = new JTextField(strValue, COLUMNS_TEXT_FIELD);
        textField.setFont(FONT_TEXT_FIELD);

        panel.add(label, Index.LABEL);
        panel.add(textField, Index.TEXT_FIELD);

        return panel;
    }

    @Override
    public String getName(final JPanel parameterPanel)
    {
        return ((JLabel)parameterPanel.getComponent(Index.LABEL)).getText();
    }

    @Override
    public Integer getValue(final JPanel parameterPanel)
    {
        String strValue = ((JTextField) parameterPanel.getComponent(Index.TEXT_FIELD)).getText();
        return Integer.valueOf(strValue);
    }

    @Override
    public void setValue(JPanel parameterPanel, Number value)
    {
        String strValue = String.valueOf(value);
        ((JTextField) parameterPanel.getComponent(Index.TEXT_FIELD)).setText(strValue);
    }
}
