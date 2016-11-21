package homemade.menu.model.settings;

class ValidParameter<Type> extends Parameter<Type> {
    protected ValueChecker<Type> valueChecker = null;

    public ValidParameter(final ValueChecker<Type> valueChecker, final String name) {
        super(valueChecker.getType(), name);

        this.valueChecker = valueChecker;
    }

    public void setName(final String newName) {
        name = newName;
    }

    public void setValue(final Type newValue) {
        if (valueChecker.isValidValue(newValue)) {
            value = newValue;
        }
    }

    public void setValueWithCast(Object newValue) {
        setValue(type.cast(newValue));
    }

    public void setDefaultValue() {
        value = valueChecker.getDefaultValue();
    }

    public void setValidValue(final Type newValue) {
        value = valueChecker.getValidValue(newValue);
    }

    /*public void setValueChecker(final ValueChecker<Type> valueChecker) {
        this.valueChecker = valueChecker;
    }*/

    public Diapason<Type> getDiapason() {
        return valueChecker.getDiapason();
    }
}
