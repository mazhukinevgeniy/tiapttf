package homemade.menu.controller.records;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.HandlerButtons;
import homemade.menu.controller.MenuManager;
import homemade.menu.model.records.Records;
import homemade.menu.view.MenuPanel;
import homemade.menu.view.records.RecordsMenu;

import java.util.EnumMap;
import java.util.Map;

public class RecordsManager implements HandlerButtons
{
    public enum CodeButton
    {
        RESET,
        BACK_TO_MENU
    }

    private MenuManager manager;

    private RecordsMenu recordsMenu;

    public RecordsManager(MenuManager manager, Records records)
    {
        this.manager = manager;
        ButtonActionListener actionListener = new ButtonActionListener(this);

        Map<CodeButton, String> buttons = createButtonsMap();
        recordsMenu = new RecordsMenu(records.getRecords(), buttons, actionListener);
    }

    private Map<CodeButton, String> createButtonsMap()
    {
        Map<CodeButton, String> buttons = new EnumMap<>(CodeButton.class);
        buttons.put(CodeButton.RESET, "Reset");
        buttons.put(CodeButton.BACK_TO_MENU, "Back to menu");

        return  buttons;
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
