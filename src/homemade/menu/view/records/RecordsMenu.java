package homemade.menu.view.records;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.records.RecordsManager.CodeButton;
import homemade.menu.model.records.Record;
import homemade.menu.view.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecordsMenu extends MenuPanel
{
    private List<Record> records = null;
    private Map<CodeButton, String> buttons;
    private ButtonActionListener actionListener;

    public RecordsMenu(List<Record> records, Map<CodeButton, String> buttons,
                       ButtonActionListener actionListener)
    {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.records = records;
        this.buttons = buttons;
        this.actionListener = actionListener;

        initializeUI();
    }

    private void initializeUI()
    {
        drawCapTable();
        drawRecordsTable();
        initializeButtonPanel(buttons, actionListener);
    }

    private void drawCapTable()
    {
        JPanel newRow = RowRecordFactory.create(CupTable.PLACE, CupTable.PLAYER_NAME, CupTable.SCORE);
        add(newRow);
    }

    private void drawRecordsTable()
    {
        int size = records.size();
        for (int i = 0; i < size; ++i)
        {
            Record record = records.get(i);
            String placeNumber = String.valueOf(i + 1);
            String score = String.valueOf(record.getScore());
            JPanel newRow = RowRecordFactory.create(placeNumber, record.getPlayerName(), score);
            add(newRow);
        }
    }

    private void initializeButtonPanel(Map<CodeButton, String> buttons,
                                       ButtonActionListener actionListener)
    {
        JPanel panel = new JPanel(new FlowLayout());

        Set<CodeButton> keys = buttons.keySet();
        for (CodeButton key : keys)
        {
            String nameButton = buttons.get(key);
            JButton button = RecordsButtonFactory.createButton(nameButton, actionListener);
            button.setActionCommand(String.valueOf(key.ordinal()));
            panel.add(button);
        }
        add(panel);
    }

    public void updateMenu(List<Record> records)
    {
        this.records = records;
        removeAll();
        initializeUI();
        updateUI();
    }

    /**
     * Called by MenuManager when swithing to another menu
     */
    @Override
    public void onQuit()
    {

    }

    private static class CupTable
    {
        private static String PLACE = "№";
        private static String PLAYER_NAME = "Player name";
        private static String SCORE = "Score";
    }
}
