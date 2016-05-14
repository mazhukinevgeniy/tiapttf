package homemade.menu.model.settings;

class Parameter<Type> implements ParameterReadonly<Type>
{
    protected String name = null;
    protected Type value = null;
    protected ValueChecker<Type> valueChecker = null;

    public Parameter(final String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public void setName(final String newName)
    {
        name = newName;
    }

    @Override
    public Type getValue()
    {
        return value;
    }

    @Override
    public Diapason<Type> getDiapason()
    {
        return valueChecker;
    }

    public void setValue(final Type newValue)
    {
        if (valueChecker.isValidValue(newValue))
        {
            value = newValue;
        }
    }

    public void setDefaultValue()
    {
        value = valueChecker.getDefaultValue();
    }

    public void setValidValue(final Type newValue)
    {
        value = valueChecker.getValidValue(newValue);
    }

    public void setValueChecker(final ValueChecker<Type> valueChecker)
    {
        this.valueChecker = valueChecker;
    }
}
