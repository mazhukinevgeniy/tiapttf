package homemade.menu.model.records;

import homemade.menu.model.save.RecordsSave;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Records
{
    private static final int MAX_NUMBER_OF_RECORDS = 10;
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
        if(records.size() > MAX_NUMBER_OF_RECORDS)
        {
            shrinkToFit();
            rewriteSave();
        }
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

    private void shrinkToFit()
    {
        int size = records.size();
        int afterLast = MAX_NUMBER_OF_RECORDS;
        for (int i = MAX_NUMBER_OF_RECORDS; i < size; ++i)
        {
            records.remove(afterLast);
        }
    }

    private void rewriteSave()
    {
        save.deleteAllRecords();
        for (Record rec : records)
        {
            addRecordToSave(rec);
        }
    }

    public void add(int score, String playerName, LocalDateTime dataTime)
    {
        Record record = new Record(score, playerName, dataTime.toString());
        int index = findEligiblePlace(record.getScore());
        records.add(index, record);
        if (records.size() > MAX_NUMBER_OF_RECORDS)
        {
            shrinkToFit();
            rewriteSave();
        }
        else
        {
            addRecordToSave(record);
        }
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

    public void setDefaultRecords()
    {
        save.deleteAllRecords();
        records.clear();
        records.addAll(ImaginaryRecords.records);
        sortRecords();
        rewriteSave();
    }

    private static class ImaginaryRecords
    {
        private static List<Record> records = new ArrayList<>();

        static
        {
            String now = LocalDateTime.now().toString();
            records.add(new Record(5, "Прохожий", now));
            records.add(new Record(15, "Ловкая Мышь", now));
            records.add(new Record(100, "Бизнескот", now));
            records.add(new Record(235, "Одноногий Голубь", now));
            records.add(new Record(345, "Лохматый Кентавр", now));

            records.add(new Record(450, "Испания", now));
            records.add(new Record(500, "Антон", now));
            records.add(new Record(750, "Одноглазый Волшебник", now));
            records.add(new Record(1000, "Титан", now));
            records.add(new Record(10000, "Злой Джин", now));
        }

    }
}
