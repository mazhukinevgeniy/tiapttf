package homemade.shell.model;

/**
 * Created by Marid on 27.03.2016.
 */
public class IntRange extends Range<Integer>
{
    public IntRange(Integer begin, Integer end)
    {
        super(begin, end);
    }
    public IntRange()
    {
        super();
    }

    @Override
    public  boolean isBelong(Integer element)
    {
        return begin <= element && element <= end;
    }
}
