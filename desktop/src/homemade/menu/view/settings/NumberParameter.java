package homemade.menu.view.settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

class NumberParameter implements ParameterFactory<JPanel, Integer> {
    private static class Index {
        private static final int LABEL = 0;
        private static final int TEXT_FIELD = 1;
    }

    private static final Font FONT_TEXT_FIELD = new Font("Verdana", Font.PLAIN, 13);
    private static final int COLUMNS_TEXT_FIELD = 5;

    public NumberParameter() {
    }

    @Override
    public JPanel create(final String nameParameter, final Integer value) {
        JPanel panel = new JPanel(new FlowLayout());

        JLabel label = new JLabel(nameParameter);
        String strValue = String.valueOf(value);
        JTextField textField = new JTextField(strValue, COLUMNS_TEXT_FIELD);
        textField.setFont(FONT_TEXT_FIELD);
        textField.addFocusListener(new TextFieldProofreader());

        panel.add(label, Index.LABEL);
        panel.add(textField, Index.TEXT_FIELD);

        return panel;
    }

    @Override
    public String getName(final JPanel parameterPanel) {
        return ((JLabel) parameterPanel.getComponent(Index.LABEL)).getText();
    }

    @Override
    public Integer getValue(final JPanel parameterPanel) {
        String strValue = ((JTextField) parameterPanel.getComponent(Index.TEXT_FIELD)).getText();
        return Integer.valueOf(strValue);
    }

    @Override
    public void setValue(JPanel parameterPanel, Integer value) {
        String strValue = String.valueOf(value);
        ((JTextField) parameterPanel.getComponent(Index.TEXT_FIELD)).setText(strValue);
    }

    private static class TextFieldProofreader implements FocusListener {
        private JTextField textField;
        private Integer lastValue;

        @Override
        public void focusGained(FocusEvent e) {
            textField = (JTextField) e.getComponent();
            lastValue = Integer.valueOf(textField.getText());
        }

        @Override
        public void focusLost(FocusEvent e) {
            String strValue = textField.getText();

            Integer value = lastValue;
            try {
                value = Integer.valueOf(strValue);
            } catch (Exception exception) {
            }

            textField.setText(String.valueOf(value));
        }
    }
}
