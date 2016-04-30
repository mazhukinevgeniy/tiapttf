import homemade.menu.controller.MenuManager;
import homemade.menu.model.records.Records;
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

        Records records = new Records(save);
        records.print();

        Window window = new Window();
        window.setVisible(true);

        MenuManager menuManager = new MenuManager(window, settings);

        window.finaleInitialize();
    }
}
