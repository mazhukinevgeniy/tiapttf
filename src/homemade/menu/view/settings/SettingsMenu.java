package homemade.menu.view.settings;

import homemade.menu.controller.MenuManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.Menu;

import javax.swing.*;
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

    private void createBoolSetting(String nameSetting)
    {
        JCheckBox checkBox = new JCheckBox(nameSetting);
        add(checkBox);
    }

    private void createNumberSetting(String nameSetting)
    {
        JLabel label = new JLabel(nameSetting);
        JTextField textField = new JTextField(nameSetting);
        add(label);
        add(textField);
    }
}
