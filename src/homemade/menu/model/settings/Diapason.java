package homemade.menu.model.settings;

public class Diapason<Type>
{
    private Type min;
    private Type max;

    public Diapason(Type min, Type max)
    {
        this.min = min;
        this.max = max;
    }

    public Type getMin()
    {
        return min;
    }

    public Type getMax()
    {
        return max;
    }
}
