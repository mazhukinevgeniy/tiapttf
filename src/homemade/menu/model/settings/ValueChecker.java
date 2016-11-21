package homemade.menu.model.settings;

abstract class ValueChecker<Type> {
    private Class<Type> type;

    ValueChecker(Type exampleValue) {
        type = (Class<Type>) exampleValue.getClass();
    }

    public Class<Type> getType() {
        return type;
    }

    abstract public Type getDefaultValue();

    abstract Type getValidValue(final Type uncheckedValue);

    abstract boolean isValidValue(final Type value);

    public Diapason<Type> getDiapason() {
        return null;
    }
}
