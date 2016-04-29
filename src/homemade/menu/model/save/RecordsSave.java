package homemade.menu.model.save;

import homemade.menu.model.records.Record;

import java.util.List;

/**
 * Created by Marid on 29.04.2016.
 */
public interface RecordsSave
{
    List<Record> getRecords();

    void addRecord(Record record);
}
