package homemade.shell.model;

/**
 * Created by Marid on 27.03.2016.
 */
public class Parameter<Type>
{
    public static final String INDEFINITE_NAME = "";

    protected String name;
    protected Type value;

    public Parameter(final String name, final Type value)
    {
        this.name = name;
        this.value = value;
    }

    public Parameter()
    {
        name = INDEFINITE_NAME;
        value = null;
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
        value = newValue;
    }
}
