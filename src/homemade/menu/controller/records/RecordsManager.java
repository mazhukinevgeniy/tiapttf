package homemade.menu.controller.records;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.HandlerButtons;
import homemade.menu.controller.MenuManager;
import homemade.menu.model.records.Records;
import homemade.menu.view.MenuPanel;
import homemade.menu.view.records.RecordsMenu;

/**
 * Created by Marid on 01.05.2016.
 */
public class RecordsManager implements HandlerButtons
{
    private MenuManager manager;


    private ButtonActionListener actionListener;
    private RecordsMenu recordsMenu;

    private RecordsManager() {}

    public RecordsManager(MenuManager manager, Records records)
    {
        this.manager = manager;
        actionListener = new ButtonActionListener<>(this);

        recordsMenu = new RecordsMenu(actionListener, records.getRecords());
    }

    public MenuPanel getRecordsMenu()
    {
        return recordsMenu;
    }

    @Override
    public void handleButtonClick(int codeButton)
    {

    }
}
