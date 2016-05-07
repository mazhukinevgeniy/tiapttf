package homemade.menu.model.records;

import homemade.menu.model.save.RecordsSave;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marid on 29.04.2016.
 */
public class Records
{

    private List<Record> records = new ArrayList<>();
    private RecordsSave save;

    public Records(RecordsSave save)
    {
        this.save = save;
        initializeRecords();
    }

    private void initializeRecords()
    {
        records = save.getRecords();
        sortRecords();
    }

    private void sortRecords()
    {
        int size = records.size();
        for (int i = 0; i < size; ++i)
        {
            Record recordI = records.get(i);
            for (int j = i + 1; j < size; ++j)
            {
                Record recordJ = records.get(j);
                if (recordI.getScore() < recordJ.getScore())
                {
                    records.remove(i);
                    records.add(i, recordJ);
                    records.remove(j);
                    records.add(j, recordI);
                    recordI = recordJ;
                }
            }
        }
    }

    public void add(int score, String playerName, LocalDateTime dataTime)
    {
        Record record = new Record(score, playerName, dataTime.toString());
        int index = findEligiblePlace(record.getScore());
        records.add(index, record);
        addRecordToSave(record);
    }

    private int findEligiblePlace(int score)
    {
        int size = records.size();
        int i;
        for (i = 0; i < size; ++i)
        {
            Record recordI = records.get(i);
            if (score > recordI.getScore())
            {
                break;
            }
        }

        return i;
    }

    private void addRecordToSave(Record record)
    {
        save.addRecord(record);
    }

    public List<Record> getRecords()
    {
        return new ArrayList<>(records);
    }

    public void emptyOut()
    {
        save.deleteAllRecords();
        records.clear();
    }
}
