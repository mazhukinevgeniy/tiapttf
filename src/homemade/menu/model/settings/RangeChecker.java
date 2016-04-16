package homemade.menu.model.settings;

/**
 * Created by Marid on 27.03.2016.
 */
public class RangeChecker<Type extends Comparable<Type>> implements ValueChecker<Type>
{
    protected Type begin;
    protected Type end;

    public RangeChecker(Type begin, Type end)
    {
        this.begin = begin;
        this.end = end;
    }

    public RangeChecker()
    {
        this.begin = null;
        this.end = null;
    }

    public boolean isValid()
    {
        return begin != null && end != null;
    }

    @Override
    public Type getValidValue(Type uncheckedValue)
    {
        Type checkedValue = uncheckedValue;
        if (uncheckedValue.compareTo(begin) < 0)
        {
            checkedValue = begin;
        }
        else if (uncheckedValue.compareTo(end) > 0)
        {
            checkedValue = end;
        }
        return checkedValue;
    }

    @Override
    public boolean isValidValue(Type value)
    {
        return begin.compareTo(value) <= 0 && value.compareTo(end) <= 0;
    }
}
