package homemade.menu.model.settings;

public class Parameter<Type> {
    protected Class<Type> type;
    protected String name = null;
    protected Type value = null;

    public Parameter(Class<Type> type, final String name) {
        this.type = type;
        this.name = name;
    }

    public Parameter(final String name, Type value) {
        this.type = (Class<Type>) value.getClass();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Type getValue() {
        return value;
    }

    public Class<Type> getType() {
        return type;
    }
}
