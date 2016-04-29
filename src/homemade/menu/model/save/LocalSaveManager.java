package homemade.menu.model.save;

import homemade.menu.model.records.Record;
import homemade.utils.Serializer;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Marid on 16.04.2016.
 */
public class LocalSaveManager implements SettingsSave, RecordsSave
{
    private Save save = null;

    private LocalSaveManager () {}

    public LocalSaveManager(String pathToSave)
    {
        save = new Save(pathToSave);
    }

    boolean isValid()
    {
        return save != null;
    }

    @Override
    public Integer getIntSettingsValue(String parameterName)
    {
        return getValue(Block.SETTINGS, parameterName, Integer.TYPE);
    }

    @Override
    public Boolean getBoolSettingsValue(String parameterName)
    {
        return getValue(Block.SETTINGS, parameterName, Boolean.TYPE);
    }

    @Override
    public void setSettingsValue(String parameterName, Object value)
    {
        String stringValue = value.toString();
        save.setParameterValue(Block.SETTINGS, parameterName, stringValue);
    }

    private <T> T getValue(String blockName, String parameterName, Type type)
    {
        String value = save.getParameterValue(blockName, parameterName);
        T parameterValue = convertStrValue(value, type);

        return parameterValue;
    }

    private <T> T convertStrValue(String value, Type type)
    {
        T convertedValue = null;
        if(value != null)
        {
            if (type.getTypeName().equals(Boolean.TYPE.getTypeName()))
            {
                convertedValue = (T)Boolean.valueOf(value);
            }
            else if (type.getTypeName().equals(Integer.TYPE.getTypeName()))
            {
                convertedValue = (T)Integer.valueOf(value);
            }
        }
        return convertedValue;
    }

    @Override
    public List<Record> getRecords()
    {
        return null;
    }

    @Override
    public void addRecord(Record record)
    {
        Serializer<Record> serializer = new Serializer<>();
        String serialisedRecord = serializer.serialize(record);
        save.addParameter(Block.RECORDS, "record", serialisedRecord);
    }

    //there you may add new name blocks to save
    public final class Block
    {
        public static final String SETTINGS = "settings";
        public static final String RECORDS = "records";
    }
}
