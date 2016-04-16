package homemade.menu.model.settings;

/**
 * Created by Marid on 16.04.2016.
 */
public interface ValueChecker<Type>
{
    Type getValidValue(Type uncheckedValue);
    boolean isValidValue(Type value);
}
