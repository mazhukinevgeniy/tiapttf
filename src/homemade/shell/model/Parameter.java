package homemade.shell.model;

/**
 * Created by Marid on 27.03.2016.
 */
public class Parameter<Type>
{
    public static final String INDEFINITE_NAME = "";

    protected static final int INDEFINITE_INDEX = -1;

    protected String name = INDEFINITE_NAME;
    protected Type value = null;
    protected Range<Type> range = null;
    protected Enum<Type> enumeration = null;

    public Parameter() {}

    public Parameter(final String name, final Type value)
    {
        this.name = name;
        this.value = value;
    }

    public Parameter(final String name, final Type value, final Range<Type> range)
    {
        this.name = name;
        this.value = value;
        this.range = range;
    }

    public Parameter(final String name, final Type value, final Enum<Type> enumeration)
    {
        this.name = name;
        this.value = value;
        this.enumeration = enumeration;
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
        if (isCorrect(newValue))
        {
            value = newValue;
        }
    }

    protected boolean isCorrect(final Type value)
    {
        boolean correct = true;
        if (range != null)
        {
            correct = range.isBelong(value);
        }
        if (enumeration != null)
        {
            correct = enumeration.isBelong(value);
        }

        return correct;
    }

    public void setRange(final Range<Type> range)
    {
        this.range = range;
        this.enumeration = null;
    }

    public void setEnum(final Enum<Type> enumeration)
    {
        this.enumeration = enumeration;
        this.range = null;
    }
}
