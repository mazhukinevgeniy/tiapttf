package homemade.menu.model.settings;

public class Parameter<Type> {
    protected Class<Type> type;
    protected Settings.Code code;
    protected Type value = null;

    public Parameter(Class<Type> type, Settings.Code code) {
        this.type = type;
        this.code = code;
    }

    public Parameter(Settings.Code code, Type value) {
        this.type = (Class<Type>) value.getClass();
        this.code = code;
        this.value = value;
    }

    public Settings.Code getCode() {
        return code;
    }

    public Type getValue() {
        return value;
    }

    public Class<Type> getType() {
        return type;
    }
}
