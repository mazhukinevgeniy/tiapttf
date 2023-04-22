import homemade.menu.controller.MenuManager;
import homemade.menu.model.records.Records;
import homemade.menu.model.save.LocalSaveManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.Window;

public class Main
{
    public static void main(String [] args)
    {
        LocalSaveManager save = new LocalSaveManager("save.xml");
        Settings settings = new Settings(save);

        Records records = new Records(save);

        Window window = new Window();
        window.setVisible(true);

        new MenuManager(window, settings, records);

        window.finaleInitialize();
    }
}