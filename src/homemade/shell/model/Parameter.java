package homemade.shell.model;

/**
 * Created by Marid on 27.03.2016.
 */
public class Parameter<Type>
{
    public static final String INDEFINITE_NAME = "";

    protected String name;
    protected Type value;
    protected Range<Type> range;

    public Parameter()
    {
        name = INDEFINITE_NAME;
        value = null;
        range = null;
    }

    public Parameter(final String name, final Type value)
    {
        this.name = name;
        this.value = value;
        this.range = null;
    }

    public Parameter(final String name, final Type value, final Range<Type> range)
    {
        this.name = name;
        this.value = value;
        this.range = range;
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
        if(isCorrect(newValue))
        {
            value = newValue;
        }
    }

    protected boolean isCorrect(final Type value)
    {
        boolean correct = true;
        if(range != null)
        {
            correct = range.isBelong(value);
        }

        return correct;
    }

    public void setRange(final Range<Type> range)
    {
        this.range = range;
    }
}
