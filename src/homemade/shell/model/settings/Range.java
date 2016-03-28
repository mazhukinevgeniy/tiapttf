package homemade.shell.model.settings;

/**
 * Created by Marid on 27.03.2016.
 */
public abstract class Range<Type>
{
    protected Type begin;
    protected Type end;

    public Range(Type begin, Type end)
    {
        this.begin = begin;
        this.end = end;
    }

    public Range()
    {
        this.begin = null;
        this.end = null;
    }

    public boolean isValid()
    {
        return begin != null && end != null;
    }

    public abstract boolean isBelong(Type element);
}
