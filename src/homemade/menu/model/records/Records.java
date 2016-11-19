package homemade.menu.model.records;

import homemade.menu.model.save.RecordsSave;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//TODO: make sure it's synchronized and everything is saved when user closes application
public class Records {
    private static final int MAX_NUMBER_OF_RECORDS = 10;

    private List<Record> records = new ArrayList<>();
    private RecordsSave save;

    public Records(RecordsSave save) {
        this.save = save;
        initializeRecords();
    }

    private void initializeRecords() {
        records = save.getRecords();

        if (records.size() < MAX_NUMBER_OF_RECORDS) {
            for (Record record : DefaultRecords.records)
                if (!records.contains(record)) {
                    records.add(record);
                }
        }

        Collections.sort(records);
        cullRecords();
        rewriteSave();
    }

    private void cullRecords() {
        int size = records.size();
        records.subList(MAX_NUMBER_OF_RECORDS, size).clear();
    }

    private void rewriteSave() {
        save.deleteAllRecords();
        for (Record rec : records) {
            save.addRecord(rec);
        }
    }

    public void add(int score, String playerName, LocalDateTime dateTime) {
        Record record = new Record(score, playerName, dateTime);

        int index = Collections.binarySearch(records, record);
        index = index < 0 ? -(index + 1) : index; //see binarySearch docs

        records.add(index, record);
        cullRecords();

        if (new Random().nextInt(10) < 1) //we don't want to rewrite save every time but we can't let it just grow
        {
            rewriteSave();
        } else {
            save.addRecord(record);
        }
    }

    public List<Record> getRecords() {
        return new ArrayList<>(records);
    }

    public void setDefaultRecords() {
        records = new ArrayList<>(DefaultRecords.records);
        Collections.sort(records);
        rewriteSave();
    }

    private static class DefaultRecords {
        private static List<Record> records = new ArrayList<>();

        static {
            LocalDateTime longTimeAgo = LocalDateTime.MIN;

            records.add(new Record(5, "Прохожий", longTimeAgo));
            records.add(new Record(15, "Ловкая Мышь", longTimeAgo));
            records.add(new Record(100, "Бизнескот", longTimeAgo));
            records.add(new Record(235, "Одноногий Голубь", longTimeAgo));
            records.add(new Record(345, "Лохматый Кентавр", longTimeAgo));

            records.add(new Record(450, "Испания", longTimeAgo));
            records.add(new Record(500, "Антон", longTimeAgo));
            records.add(new Record(750, "Mad Wizard", longTimeAgo));
            records.add(new Record(1000, "Титан", longTimeAgo));
            records.add(new Record(10000, "Злой Джинн", longTimeAgo));
        }
    }
}
