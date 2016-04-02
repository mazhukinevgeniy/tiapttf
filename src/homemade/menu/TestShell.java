package homemade.menu;


import homemade.menu.controller.MenuManager;
import homemade.menu.model.save.Save;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.Window;
import homemade.resources.Assets;

/**
 * Created by Marid on 27.03.2016.
 */
public class TestShell
{
    public static void main(String [] args)
    {
        Assets.loadAssets();

        Window window = new Window();
        window.setVisible(true);

        MenuManager menuManager = new MenuManager(window);

        window.finaleInitialize();
    }

    private static void parameterTest(Save save)
    {
        Settings settings = new Settings();

        System.out.println(settings.getIsRealTime());
        System.out.println(settings.getSimultaneousSpawn());
        System.out.println(settings.getSpawnPeriod());

        settings = new Settings(save);
        System.out.println();

        System.out.println(settings.getIsRealTime());
        System.out.println(settings.getSimultaneousSpawn());
        System.out.println(settings.getSpawnPeriod());

        settings.setIsRealTime(false);
        settings.setSpawnPeriod(1500);

        System.out.println(settings.getIsRealTime());
        System.out.println(settings.getSimultaneousSpawn());
        System.out.println(settings.getSpawnPeriod());
    }
}
