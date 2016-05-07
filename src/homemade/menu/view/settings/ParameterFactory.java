package homemade.menu.view.settings;

interface ParameterFactory<TypeUI, TypeValue>
{
    TypeUI create(final String nameParameter, final TypeValue value);

    String getName(final  TypeUI parameter);
    void setValue(TypeUI parameterPanel, TypeValue value);

    TypeValue getValue(final TypeUI parameter);
}
