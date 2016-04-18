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
        Settings settings = new Settings(save);

        Window window = new Window();
        window.setVisible(true);

        MenuManager menuManager = new MenuManager(window, settings);

        window.finaleInitialize();
    }

    private static void parameterTest(LocalSaveManager save)
    {
        Settings settings = new Settings();

        Boolean isRealTime1 = true;
        settings.get(Settings.Name.isRealTime, isRealTime1);

        System.out.println(isRealTime1);//settings.get(Settings.Name.isRealTime, Boolean.TYPE));
        System.out.println((Integer) settings.get(Settings.Name.simultaneousSpawn));
        System.out.println((Integer) settings.get(Settings.Name.spawnPeriod));

        settings = new Settings(save);
        System.out.println();


        boolean isRealTime = settings.get(Settings.Name.isRealTime);
        int simultaneousSpawn = settings.get(Settings.Name.simultaneousSpawn);
        int spawnPeriod = settings.get(Settings.Name.spawnPeriod);
        System.out.println(isRealTime);
        System.out.println(simultaneousSpawn);
        System.out.println(spawnPeriod);

        settings.set(Settings.Name.isRealTime, !isRealTime);
        settings.set(Settings.Name.simultaneousSpawn, ++simultaneousSpawn);
        settings.set(Settings.Name.spawnPeriod, ++spawnPeriod);

        System.out.println((Boolean) settings.get(Settings.Name.isRealTime));
        System.out.println((Integer) settings.get(Settings.Name.simultaneousSpawn));
        System.out.println((Integer) settings.get(Settings.Name.spawnPeriod));

        System.out.println(settings.getType(Settings.Name.isRealTime).getTypeName());
    }
}
