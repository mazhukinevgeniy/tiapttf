package homemade.shell.model;

import java.util.Vector;

/**
 * Created by Marid on 28.03.2016.
 */
public class Enum<Type>
{
    private static final int INDEFINITE_INDEX = -1;

    private Vector<Type> elements = null;

    public Enum(final Type... elements)
    {
        this.elements = new Vector<>();
        for (Type element : elements)
        {
            this.elements.add(element);
        }
    }

    public boolean isValid()
    {
        return elements != null;
    }

    public boolean isBelong(final Type element)
    {
        boolean belong = false;
        if (isValid())
        {
            int index = elements.indexOf(element);
            if(index != INDEFINITE_INDEX)
            {
                belong = true;
            }
        }

        return belong;
    }
}
