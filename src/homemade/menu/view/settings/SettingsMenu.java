package homemade.menu.view.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.view.Menu;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Marid on 18.04.2016.
 */
public class SettingsMenu extends Menu
{
    BoolParameter boolParameter = new BoolParameter();
    NumberParameter numberParameter = new NumberParameter();

    Map<String, Pair<Type, ?>> parameters;

    Vector<JCheckBox> checkBoxes = new Vector<>();
    Vector<JPanel> parameterPanels = new Vector<>();

    Map<Integer, String> buttons;
    ButtonActionListener actionListener;

    private SettingsMenu() {}

    public SettingsMenu(Map<String, Pair<Type, ?>> parameters, Map<Integer, String> buttons,
                        ButtonActionListener actionListener)
    {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.parameters = parameters;
        this.buttons = buttons;
        this.actionListener = actionListener;

        redrawUI();
    }

    public void redrawUI()
    {
        checkBoxes.clear();
        parameterPanels.clear();
        removeAll();
        initializeParametersUI();
        drawParametersUI();
        initializeButtonPanel(buttons, actionListener);
        updateUI();
    }

    private void initializeParametersUI()
    {
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
        JCheckBox checkBox = boolParameter.create(parameterName, value);
        checkBoxes.add(checkBox);
    }

    private void createNumberSetting(String parameterName, Integer value)
    {
        JPanel panel = numberParameter.create(parameterName, value);
        parameterPanels.add(panel);
    }

    private void drawParametersUI()
    {
        for (JCheckBox checkBox : checkBoxes)
        {
            add(checkBox);
        }
        for (JPanel panel : parameterPanels)
        {
            add(panel);
        }
    }

    private void initializeButtonPanel(Map<Integer, String> buttons, ButtonActionListener actionListener)
    {
        JPanel panel = new JPanel(new FlowLayout());

        Set<Integer> keys = buttons.keySet();
        for (int key : keys)
        {
            String nameButton = buttons.get(key);
            JButton button = SettingsButtonFactory.createButton(nameButton, actionListener);
            button.setActionCommand(String.valueOf(key));
            panel.add(button);
        }
        add(panel);
    }

    public Map<String, Pair<Type, ?>> getParameters()
    {
        Map<String, Pair<Type, ?>> newParameters = new HashMap<>();
        for (JCheckBox checkBox : checkBoxes)
        {
            String parameterName = boolParameter.getName(checkBox);
            Pair<Type, ?> pair = parameters.get(parameterName);
            pair = new Pair<>(pair.getKey(), boolParameter.getValue(checkBox));
            newParameters.put(parameterName, pair);
        }
        for (JPanel panel : parameterPanels)
        {
            String parameterName = numberParameter.getName(panel);
            Pair<Type, ?> pair = parameters.get(parameterName);
            pair = new Pair<>(pair.getKey(), numberParameter.getValue(panel));
            newParameters.put(parameterName, pair);
        }
        parameters = newParameters;

        return parameters;
    }

    public void setParameters(Map<String, Pair<Type, ?>> parameters)
    {
        this.parameters = parameters;
    }
}
