package homemade.menu.model.settings;

interface ValueChecker<Type> {
    Type getDefaultValue();

    Type getValidValue(final Type uncheckedValue);

    boolean isValidValue(final Type value);

    default Diapason<Type> getDiapason() {
        return null;
    }
}
