package homemade.menu.model.settings;

class ValidParameter<Type> extends Parameter<Type> {
    protected ValueChecker<Type> valueChecker = null;

    public ValidParameter(ValueChecker<Type> valueChecker, Settings.Code parameterCode) {
        super(valueChecker.getType(), parameterCode);

        this.valueChecker = valueChecker;
    }

    public void setValue(Type newValue) {
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

    public void setValidValue(Type newValue) {
        value = valueChecker.getValidValue(newValue);
    }

    public Diapason<Type> getDiapason() {
        return valueChecker.getDiapason();
    }
}
