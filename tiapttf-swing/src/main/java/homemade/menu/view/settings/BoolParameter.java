package homemade.menu.view.settings;

import javax.swing.*;
import java.awt.*;

class BoolParameter implements ParameterFactory<JCheckBox, Boolean> {
    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);

    public BoolParameter() {
    }

    @Override
    public JCheckBox create(final String parameterName, Boolean value) {
        JCheckBox checkBox = new JCheckBox(parameterName);
        checkBox.setSelected(value);
        checkBox.setHorizontalTextPosition(JCheckBox.LEFT);

        return checkBox;
    }

    @Override
    public String getName(final JCheckBox parameter) {
        return parameter.getText();
    }

    @Override
    public Boolean getValue(final JCheckBox parameter) {
        return parameter.isSelected();
    }

    @Override
    public void setValue(JCheckBox parameter, Boolean value) {
        parameter.setSelected(value);
    }
}
