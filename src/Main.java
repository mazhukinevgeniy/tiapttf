import homemade.menu.controller.MenuManager;
import homemade.menu.model.records.Records;
import homemade.menu.model.save.LocalSaveManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.Window;
import homemade.resources.Assets;

public class Main
{
    public static void main(String [] args)
    {
        Assets.loadAssets();

        LocalSaveManager save = new LocalSaveManager("save.xml");
        Settings settings = new Settings(save);

        Records records = new Records(save);

        Window window = new Window();
        window.setVisible(true);

        new MenuManager(window, settings, records);

        window.finaleInitialize();
    }
}