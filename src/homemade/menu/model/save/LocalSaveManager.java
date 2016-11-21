package homemade.menu.model.save;

import homemade.menu.model.records.Record;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalSaveManager implements SettingsSave, RecordsSave {
    private Save save = null;

    public LocalSaveManager(String pathToSave) {
        save = new Save(pathToSave);
    }

    @Override
    public Integer getIntSettingsValue(String parameterName) {
        return getValue(Block.SETTINGS, parameterName, Integer.TYPE);
    }

    @Override
    public Boolean getBoolSettingsValue(String parameterName) {
        return getValue(Block.SETTINGS, parameterName, Boolean.TYPE);
    }

    @Override
    public <Type> Type getSettingsValue(String parameterName, Class<Type> type) {
        if (type == Boolean.class) {
            return getValue(Block.SETTINGS, parameterName, Boolean.TYPE);
        } else if (type == Integer.class) {
            return getValue(Block.SETTINGS, parameterName, Integer.TYPE);
        }
        return null;
    }

    @Override
    public void setSettingsValue(String parameterName, Object value) {
        String stringValue = value.toString();
        save.setParameterValue(Block.SETTINGS, parameterName, stringValue);
    }

    private <T> T getValue(String blockName, String parameterName, Type type) {
        String value = save.getParameterValue(blockName, parameterName);
        /*Class<Integer> t = Integer.TYPE;
        Integer integer = t.cast(value);*/
        T parameterValue = convertStrValue(value, type);

        return parameterValue;
    }

    private <T> T convertStrValue(String value, Type type) {
        T convertedValue = null;
        if (value != null) {
            if (type.getTypeName().equals(Boolean.TYPE.getTypeName())) {
                convertedValue = (T) Boolean.valueOf(value);
            } else if (type.getTypeName().equals(Integer.TYPE.getTypeName())) {
                convertedValue = (T) Integer.valueOf(value);
            }
        }
        return convertedValue;
    }

    @Override
    public List<Record> getRecords() {
        List<String> strRecords = save.getParametersValues(Block.RECORDS, "record");
        List<Record> records = new ArrayList<>();
        for (String strRec : strRecords) {
            Record record = Record.valueOf(strRec);
            if (record != null) {
                records.add(record);
            }
        }

        return records;
    }

    @Override
    public void addRecord(Record record) {
        String serialisedRecord = record.toString();
        save.addParameter(Block.RECORDS, "record", serialisedRecord);
    }

    @Override
    public void deleteAllRecords() {
        save.deleteParameters(Block.RECORDS, "record");
    }

    //there you may add new name blocks to save
    private static final class Block {
        private static final String SETTINGS = "settings";
        private static final String RECORDS = "records";
    }
}
