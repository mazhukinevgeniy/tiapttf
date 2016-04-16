import homemade.menu.controller.MenuManager;
import homemade.menu.model.save.LocalSaveManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.Window;
import homemade.resources.Assets;

/**
 * Created by user3 on 22.03.2016.
 */
public class Main
{
    public static void main(String [] args)
    {
        Assets.loadAssets();

        LocalSaveManager save = new LocalSaveManager("save.xml");
        //save.getIntegerValue("test");
        parameterTest(save);

        Window window = new Window();
        window.setVisible(true);

        MenuManager menuManager = new MenuManager(window);

        window.finaleInitialize();
    }

    private static void parameterTest(LocalSaveManager save)
    {
        Settings settings = new Settings();

        System.out.println(settings.getIsRealTime());
        System.out.println(settings.getSimultaneousSpawn());
        System.out.println(settings.getSpawnPeriod());

        settings = new Settings(save);
        System.out.println();

        boolean isRealTime = settings.getIsRealTime();
        int simultaneousSpawn = settings.getSimultaneousSpawn();
        int spawnPeriod = settings.getSpawnPeriod();
        System.out.println(isRealTime);
        System.out.println(simultaneousSpawn);
        System.out.println(spawnPeriod);

        settings.setIsRealTime(!isRealTime);
        settings.setSimultaneousSpawn(++simultaneousSpawn);
        settings.setSpawnPeriod(++spawnPeriod);

        System.out.println(settings.getIsRealTime());
        System.out.println(settings.getSimultaneousSpawn());
        System.out.println(settings.getSpawnPeriod());
    }
}
