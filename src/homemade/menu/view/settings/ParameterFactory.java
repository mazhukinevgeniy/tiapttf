package homemade.menu.view.settings;

/**
 * Created by Marid on 19.04.2016.
 */
interface ParameterFactory<TypeUI, TypeValue>
{
    TypeUI create(final String nameParameter, final TypeValue value);

    String getName(final  TypeUI parameter);
    void setValue(TypeUI parameterPanel, TypeValue value);

    TypeValue getValue(final TypeUI parameter);
}
