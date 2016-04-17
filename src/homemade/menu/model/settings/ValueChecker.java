package homemade.menu.model.settings;

/**
 * Created by Marid on 16.04.2016.
 */
public interface ValueChecker<Type>
{
    Type getDefaultValue();
    Type getValidValue(final Type uncheckedValue);
    boolean isValidValue(final Type value);
}
