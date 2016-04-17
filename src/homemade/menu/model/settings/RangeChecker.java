package homemade.menu.model.settings;

/**
 * Created by Marid on 27.03.2016.
 */
class RangeChecker<Type extends Comparable<Type>> implements ValueChecker<Type>
{
    private Type defaultValue = null;
    private Type begin = null;
    private Type end = null;

    private RangeChecker() {}

    public RangeChecker(final Type defaultValue, final Type begin, final Type end)
    {
        this.begin = begin;
        this.end = end;
        setDefaultValue(defaultValue);
    }

    private void setDefaultValue(Type defaultValue)
    {
        this.defaultValue = defaultValue;
        if (!isValidValue(defaultValue))
        {
            if (defaultValue.compareTo(begin) < 0)
            {
                this.defaultValue = begin;
            }
            else if (defaultValue.compareTo(end) > 0)
            {
                this.defaultValue = end;
            }
        }
    }

    @Override
    public Type getDefaultValue()
    {
        return defaultValue;
    }

    @Override
    public Type getValidValue(final Type uncheckedValue)
    {
        Type checkedValue = uncheckedValue;
        if (!isValidValue(uncheckedValue))
        {
            checkedValue = defaultValue;
        }
        return checkedValue;
    }

    @Override
    public boolean isValidValue(final Type value)
    {
        return begin.compareTo(value) <= 0 && value.compareTo(end) <= 0;
    }
}
