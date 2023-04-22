package homemade.menu.model.settings;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class InSetChecker<Type extends Comparable<Type>> extends ValueChecker<Type> {
    private Type defaultValue = null;
    private Set<Type> elements = new HashSet<>();

    public InSetChecker(final Type defaultValue, final Type... elements) {
        super(defaultValue);

        Collections.addAll(this.elements, elements);
        sedDefaultValue(defaultValue);
    }

    private void sedDefaultValue(final Type defaultValue) {
        this.defaultValue = defaultValue;
        if (!isValidValue(defaultValue)) {
            if (this.elements.size() > 0) {
                this.defaultValue = (Type) this.elements.toArray()[0];
            } else {
                this.defaultValue = null;
            }
        }
    }

    @Override
    public Type getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Type getValidValue(final Type uncheckedValue) {
        Type checkedValue = uncheckedValue;
        if (!isValidValue(uncheckedValue)) {
            checkedValue = defaultValue;
        }

        return checkedValue;
    }

    @Override
    public boolean isValidValue(final Type value) {
        return elements.contains(value);
    }

    @Override
    public Diapason<Type> getDiapason() {
        Type min = defaultValue;
        Type max = defaultValue;
        for (Type element : elements) {
            if (element.compareTo(min) < 0) {
                min = element;
            }
            if (element.compareTo(max) > 0) {
                max = element;
            }
        }
        return new Diapason<>(min, max);
    }
}
