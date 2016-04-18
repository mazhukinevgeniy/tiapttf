package homemade.menu.view.settings;

import homemade.menu.view.Menu;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * Created by Marid on 18.04.2016.
 */
public class SettingsMenu extends Menu
{
    Map<String, Pair<Type, ?>> parameters;

    private SettingsMenu() {}

    public SettingsMenu(Map<String, Pair<Type, ?>> parameters)
    {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.parameters = parameters;

        initializeParametersUI();
        initializeButtonPanel();
    }

    private void initializeParametersUI()
    {;
        Set<String> parameterNames = parameters.keySet();

        for(String name : parameterNames)
        {
            Pair<Type, ?> pair = parameters.get(name);
            if (pair.getKey() == Boolean.TYPE)
            {
                createBoolSetting(name, (Boolean) pair.getValue());
            }
            else if (pair.getKey() == Integer.TYPE)
            {
                createNumberSetting(name, (Integer) pair.getValue());
            }
        }
    }

    private void createBoolSetting(String parameterName, Boolean value)
    {
        JCheckBox checkBox = BoolParameterFactory.createCheckBox(parameterName, value);
        add(checkBox);
    }

    private void createNumberSetting(String parameterName, Integer value)
    {
        JPanel panel = NumberParameterFactory.createParameterPanel(parameterName, String.valueOf(value));
        add(panel);
    }

    private void initializeButtonPanel()
    {
        JPanel panel = new JPanel(new FlowLayout());

        JButton defaultSettings = SettingsButtonFactory.createButton("Reset settings", null);
        panel.add(defaultSettings);

        add(panel);
    }
}
