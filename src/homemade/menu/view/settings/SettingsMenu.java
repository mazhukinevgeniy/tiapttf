package homemade.menu.view.settings;

import homemade.menu.controller.MenuManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.Menu;

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
    private Settings settings;

    private SettingsMenu() {}

    public SettingsMenu(MenuManager manager, Settings settings)
    {
        super(manager);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.settings = settings;

        initializeParametersUI();
        initializeButtonPanel();
    }

    private void initializeParametersUI()
    {
        Map<String, Type> parameterNamesMap = settings.getParemeterNamesMap();
        Set<String> parameterNames = parameterNamesMap.keySet();

        for(String name : parameterNames)
        {
            Type type = parameterNamesMap.get(name);
            if (type == Boolean.TYPE)
            {
                createBoolSetting(name);
            }
            else if (type == Integer.TYPE)
            {
                createNumberSetting(name);
            }
        }
    }

    private void createBoolSetting(String parameterName)
    {
        Boolean value = settings.get(parameterName);
        JCheckBox checkBox = BoolParameterFactory.createCheckBox(parameterName, value);
        add(checkBox);
    }

    private void createNumberSetting(String parameterName)
    {
        Integer value = settings.get(parameterName);
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
