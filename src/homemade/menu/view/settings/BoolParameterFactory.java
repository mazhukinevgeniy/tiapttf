package homemade.menu.view.settings;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marid on 18.04.2016.
 */
class BoolParameterFactory
{
    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);

    public static JCheckBox createCheckBox(final String parameterName, boolean value)
    {
        JCheckBox checkBox = new JCheckBox(parameterName);
        checkBox.setSelected(value);
        checkBox.setHorizontalTextPosition(JCheckBox.LEFT);

        return checkBox;
    }
}
