package homemade.menu.model.settings;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marid on 28.03.2016.
 */
public class InSetChecker<Type extends Comparable<Type>> implements ValueChecker<Type>
{
    private static final int INDEFINITE_INDEX = -1;

    private Type defaultValue = null;
    private Set<Type> elements = null;

    private  InSetChecker() {}

    public InSetChecker(final Type defaultValue, final Type... elements)
    {
        this.elements = new HashSet<>();
        for (Type element : elements)
        {
            this.elements.add(element);
        }
        sedDefaultValue(defaultValue);
    }

    private void sedDefaultValue(final Type defaultValue)
    {
        this.defaultValue = defaultValue;
        if(!isValidValue(defaultValue))
        {
            if (this.elements.size() > 0)
            {
                this.defaultValue = (Type) this.elements.toArray()[0];
            }
            else
            {
                this.defaultValue = null;
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
        return elements.contains(value);
    }
}
