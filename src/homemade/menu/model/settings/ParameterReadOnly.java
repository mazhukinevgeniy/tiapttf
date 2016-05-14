package homemade.menu.model.settings;

public interface ParameterReadonly<Type>
{
    String getName();
    Type getValue();
    Diapason<Type> getDiapason();
}
