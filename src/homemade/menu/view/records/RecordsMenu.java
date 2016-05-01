package homemade.menu.view.records;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.model.records.Record;
import homemade.menu.view.MenuPanel;

import javax.swing.*;
import java.util.List;

/**
 * Created by Marid on 01.05.2016.
 */
public class RecordsMenu extends MenuPanel
{
    private List<Record> records = null;

    private RecordsMenu() {}

    public RecordsMenu(ButtonActionListener actionListener, List<Record> records)
    {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.records = records;

        initializeUI();
    }

    private void initializeUI()
    {
        drawCapTable();
        drawRecordsTable();
    }

    private void drawCapTable()
    {
        JLabel newRow = RowRecordFactory.create(CupTable.PLACE, CupTable.PLAYER_NAME, CupTable.SCORE);
        add(newRow);
    }

    private void drawRecordsTable()
    {
        int size = records.size();
        for (int i = 0; i < size; ++i)
        {
            Record record = records.get(i);
            String placeNumber = String.valueOf(i);
            String score = String.valueOf(record.getScore());
            JLabel newRow = RowRecordFactory.create(placeNumber, record.getPlayerName(), score);
            add(newRow);
        }
    }

    private static class CupTable
    {
        private static String PLACE = "№";
        private static String PLAYER_NAME = "Player name";
        private static String SCORE = "Score";
    }
}
