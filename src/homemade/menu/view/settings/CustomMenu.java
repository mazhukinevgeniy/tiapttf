package homemade.menu.view.settings;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.settings.CustomManager;
import homemade.menu.model.settings.Parameter;
import homemade.menu.view.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CustomMenu extends MenuPanel
{
    private BoolParameter boolFactory = new BoolParameter();
    private NumberParameter numberFactory = new NumberParameter();

    private List<Parameter<?>> parameters;

    private Vector<JCheckBox> checkBoxes = new Vector<>();
    private Vector<JPanel> parameterPanels = new Vector<>();

    private Map<CustomManager.CodeButton, String> buttons;
    private ButtonActionListener actionListener;

    public CustomMenu (List<Parameter<?>> parameters, Map<CustomManager.CodeButton, String> buttons,
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
        for(Parameter<?> param : parameters)
        {
            if (param.getType() == Boolean.class)
            {
                createBoolSetting(param.getName(), (Boolean) param.getValue());
            }
            else if (param.getType() == Integer.class)
            {
                createNumberSetting(param.getName(), (Integer) param.getValue());
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

    private void initializeButtonPanel(Map<CustomManager.CodeButton, String> buttons, ButtonActionListener actionListener)
    {
        JPanel panel = new JPanel(new FlowLayout());

        Set<CustomManager.CodeButton> keys = buttons.keySet();
        for (CustomManager.CodeButton key : keys)
        {
            String nameButton = buttons.get(key);
            JButton button = SettingsButtonFactory.createButton(nameButton, actionListener);
            button.setActionCommand(String.valueOf(key.ordinal()));
            panel.add(button);
        }
        add(panel);
    }

    public List<Parameter<?>> getParameters()
    {
        List<Parameter<?>> newParameters = new ArrayList<>();
        addValuesTo(newParameters, checkBoxes, boolFactory, Boolean.class);
        addValuesTo(newParameters, parameterPanels, numberFactory, Integer.class);
        parameters = newParameters;

        return parameters;
    }

    private <TypeUI, TypeValue> List<Parameter<?>> addValuesTo(List<Parameter<?>> newParameters,
                                                               Vector<TypeUI> parametersUI,
                                                               ParameterFactory<TypeUI, ?> factory,
                                                               Class<TypeValue> type)
    {
        for (TypeUI parameter : parametersUI)
        {
            String name = factory.getName(parameter);
            TypeValue value = type.cast(factory.getValue(parameter));
            newParameters.add(new Parameter<>(type, name, value));
        }
        return newParameters;
    }

    public void updateMenu(List<Parameter<?>> parameters)
    {
        this.parameters = parameters;

        reinitializeParametersUI(checkBoxes, boolFactory);
        reinitializeParametersUI(parameterPanels, numberFactory);
    }

    private <TypeUI, TypeValue> void reinitializeParametersUI(Vector<TypeUI> parametersUI,
                                                              ParameterFactory<TypeUI, TypeValue> factory)
    {
        for (TypeUI parameterUI : parametersUI)
        {
            String name = factory.getName(parameterUI);
            for (Parameter<?> param : parameters)
            {
                if (param.getName().equals(name))
                {
                    TypeValue newValue = (TypeValue) param.getValue();
                    factory.setValue(parameterUI, newValue);
                }
            }
        }
    }
}
