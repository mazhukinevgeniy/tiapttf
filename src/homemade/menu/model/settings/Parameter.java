package homemade.menu.model.settings;

/**
 * Created by Marid on 27.03.2016.
 */
public class Parameter<Type>
{
    public static final String INDEFINITE_NAME = "";

    protected String name = INDEFINITE_NAME;
    protected Type value = null;
    protected ValueChecker<Type> valueChecker = null;

    public Parameter() {}

    public Parameter(final String name)
    {
        this.name = name;
    }

    public Parameter(final String name, final Type value)
    {
        this.name = name;
        this.value = value;
    }

    public Parameter(final String name, final Type value, final ValueChecker<Type> valueChecker)
    {
        this.name = name;
        this.value = value;
        this.valueChecker = valueChecker;
    }

    public boolean isValid()
    {
        return name != INDEFINITE_NAME && value != null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String newName)
    {
        name = newName;
    }

    private boolean hasValue()
    {
        return value != null;
    }

    public Type getValue()
    {
        return value;
    }

    public void setValue(final Type newValue)
    {
        if (valueChecker.isValidValue(newValue))
        {
            value = newValue;
        }
    }

    public void setNearestValidValue(final Type newValue)
    {
        value = valueChecker.getValidValue(newValue);
    }

    public void setValueChecker(final ValueChecker<Type> valueChecker)
    {
        this.valueChecker = valueChecker;
    }
}
