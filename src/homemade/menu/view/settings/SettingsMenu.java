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
    BoolParameter boolFactory = new BoolParameter();
    NumberParameter numberFactory = new NumberParameter();

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

        initializeUI();
    }

    private void initializeUI()
    {
        initializeParametersUI();
        drawParametersUI();
        initializeButtonPanel(buttons, actionListener);
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
        JCheckBox checkBox = boolFactory.create(parameterName, value);
        checkBoxes.add(checkBox);
    }

    private void createNumberSetting(String parameterName, Integer value)
    {
        JPanel panel = numberFactory.create(parameterName, value);
        parameterPanels.add(panel);
    }

    private void drawParametersUI()
    {
        drawParametersUI(checkBoxes);
        drawParametersUI(parameterPanels);
    }

    private <TypeUI extends JComponent> void drawParametersUI(Vector<TypeUI> parametersUI)
    {
        for (TypeUI parameter : parametersUI)
        {
            add(parameter);
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
        addValuesTo(newParameters, checkBoxes, boolFactory);
        addValuesTo(newParameters, parameterPanels, numberFactory);
        parameters = newParameters;

        return parameters;
    }

    private <TypeUI> Map<String, Pair<Type, ?>> addValuesTo(Map<String, Pair<Type, ?>> newParameters,
                                                            Vector<TypeUI> parameterUI,
                                                            ParameterFactory<TypeUI, ?> factory)
    {
        for (TypeUI parameter : parameterUI)
        {
            String parameterName = factory.getName(parameter);
            Pair<Type, ?> pair = parameters.get(parameterName);
            pair = new Pair<>(pair.getKey(), factory.getValue(parameter));
            newParameters.put(parameterName, pair);
        }
        return newParameters;
    }

    public void setParameters(Map<String, Pair<Type, ?>> parameters)
    {
        this.parameters = parameters;
    }

    public void updateMenu()
    {
        reinitializeParametersUI(checkBoxes, boolFactory);
        reinitializeParametersUI(parameterPanels, numberFactory);
    }

    private <TypeUI, TypeValue> void reinitializeParametersUI(Vector<TypeUI> parameterUI,
                                                              ParameterFactory<TypeUI, TypeValue> factory)
    {
        for (TypeUI parameter : parameterUI)
        {
            String parameterName = factory.getName(parameter);
            if (parameters.containsKey(parameterName))
            {
                TypeValue newValue = getValueFromParameters(parameterName);
                factory.setValue(parameter, newValue);
            }
        }
    }

    private <TypeValue> TypeValue getValueFromParameters(String parameterName)
    {
        Pair<Type, TypeValue> pair = (Pair<Type, TypeValue>) parameters.get(parameterName);
        return pair.getValue();
    }
}
