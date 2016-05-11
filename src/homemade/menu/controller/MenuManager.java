package homemade.menu.controller;

import homemade.game.GameSettings;
import homemade.game.controller.GameController;
import homemade.menu.controller.records.RecordsManager;
import homemade.menu.controller.settings.SettingsManager;
import homemade.menu.model.records.Records;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.MenuPanel;
import homemade.menu.view.Window;
import homemade.menu.view.gameMenu.GameMenu;
import homemade.menu.view.mainMenu.MainMenu;
import homemade.resources.Assets;
import homemade.resources.links.LinkAssets.Variation;

import java.util.EnumMap;
import java.util.Map;

public class MenuManager implements HandlerButtons
{
    public enum MenuCode
    {
        GAME,
        SETTINGS,
        MAIN_MENU,
        RECORDS
    }

    private Window window;
    private Settings settings;

    private MenuPanel currentMenu;
    private Map<MenuCode, MenuPanel> menus;

    private Records records;

    public MenuManager(Window window, Settings settings, Records records)
    {
        this.window = window;
        this.settings = settings;
        this.records = records;

        Map<MenuCode, String> menuNames = createMenuNamesMap();
        MenuPanel mainMenu = new MainMenu(menuNames, new ButtonActionListener(this));

        SettingsManager settingsManager = new SettingsManager(this, settings);
        MenuPanel settingsMenu = settingsManager.getSettingsMenu();

        RecordsManager recordsManager = new RecordsManager(this, records);
        MenuPanel recordsMenu = recordsManager.getRecordsMenu();

        menus = new EnumMap<>(MenuCode.class);
        menus.put(MenuCode.MAIN_MENU, mainMenu);
        menus.put(MenuCode.SETTINGS, settingsMenu);
        menus.put(MenuCode.RECORDS, recordsMenu);
        menus.put(MenuCode.GAME, new GameMenu());

        setCurrentMenu(MenuCode.MAIN_MENU);
        window.add(currentMenu);
    }

    private Map<MenuCode, String> createMenuNamesMap()
    {
        Map<MenuCode, String> menuNames = new EnumMap<>(MenuCode.class);
        menuNames.put(MenuCode.GAME, "Game");
        menuNames.put(MenuCode.SETTINGS, "Settings");
        menuNames.put(MenuCode.RECORDS, "Records");

        return menuNames;
    }

    private void setCurrentMenu(MenuCode code)
    {
        currentMenu = menus.get(code);
    }

    @Override
    public void handleButtonClick(int code)
    {
        MenuCode menuCode = MenuCode.values()[code];

        switchToMenu(menuCode);
    }

    public void switchToMenu(MenuCode code)
    {
        window.setTitle("here we go again");

        if (currentMenu != null)
            currentMenu.onQuit();

        window.remove(currentMenu);
        setCurrentMenu(code);
        window.add(currentMenu);

        if (code == MenuCode.GAME)
        {
            Variation assetType = settings.get(Settings.Name.animatedLinks) ? Variation.ANIMATED : Variation.COLORED;
            Assets.loadAssets(assetType);

            new GameController(this, window, currentMenu, new GameSettings(settings), records);
        }

        currentMenu.onShown();
        currentMenu.updateUI();
    }
}
