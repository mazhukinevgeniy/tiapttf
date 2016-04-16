package homemade.menu.model.settings;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marid on 28.03.2016.
 */
public class InSetChecker<Type extends Comparable<Type>> implements ValueChecker<Type>
{
    private static final int INDEFINITE_INDEX = -1;

    private Set<Type> elements = null;

    public InSetChecker(final Type... elements)
    {
        this.elements = new HashSet<>();
        for (Type element : elements)
        {
            this.elements.add(element);
        }
    }

    public boolean isValid()
    {
        return elements != null;
    }

    @Override
    public Type getValidValue(Type uncheckedValue)
    {
        Type checkedValue = uncheckedValue;
        if (!isValidValue(uncheckedValue))
        {
            checkedValue = (Type)elements.toArray()[0];
        }

        return checkedValue;
    }

    @Override
    public boolean isValidValue(Type value)
    {
        return elements.contains(value);
    }
}
