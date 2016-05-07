package homemade.menu.model.save;

import homemade.menu.model.records.Record;

import java.util.List;

public interface RecordsSave
{
    List<Record> getRecords();

    void addRecord(Record record);
    void deleteAllRecords();
}
