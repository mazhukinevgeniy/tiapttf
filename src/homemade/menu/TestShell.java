package homemade.menu;


import homemade.menu.model.save.Save;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.MainMenu;
import homemade.menu.view.Window;

/**
 * Created by Marid on 27.03.2016.
 */
public class TestShell
{
    public static void main(String [] args)  throws Exception
    {
        createGUI();

        Save save = new Save("settings.xml");
        parameterTest(save);
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

    public static void createGUI() {
        Window window = new Window();
        MainMenu menu = new MainMenu();

        window.add(menu);

        window.finalize();
        window.setVisible(true);
    }
}
