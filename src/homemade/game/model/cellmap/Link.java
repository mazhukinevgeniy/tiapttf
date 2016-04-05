package homemade.game.model.cellmap;

/**
 * Created by user3 on 27.03.2016.
 */
public class Link
{
    boolean value = false;

    private int number;

    Link(int number)
    {
        this.number = number;
    }

    public int getNumber()
    {
        return number;
    }

    public boolean getValue()
    {
        return value;
    }
}
