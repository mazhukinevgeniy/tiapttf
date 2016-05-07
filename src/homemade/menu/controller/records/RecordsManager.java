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
    private Records records;

    public RecordsManager(MenuManager manager, Records records)
    {
        this.manager = manager;
        this.records = records;
        ButtonActionListener actionListener = new ButtonActionListener(this);

        Map<CodeButton, String> buttons = createButtonsMap();
        recordsMenu = new RecordsMenu(this, records.getRecords(), buttons, actionListener);
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

    public void requestUpdate()
    {
        recordsMenu.updateMenu(records.getRecords());
    }

    @Override
    public void handleButtonClick(int code)
    {
        CodeButton codeButton = CodeButton.values()[code];

        if (codeButton == CodeButton.BACK_TO_MENU)
        {
            manager.switchToMenu(MenuManager.MenuCode.MAIN_MENU);
        }
        else if (codeButton == CodeButton.RESET)
        {
            records.emptyOut();
            recordsMenu.updateMenu(records.getRecords());
        }
    }
}
