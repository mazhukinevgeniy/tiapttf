package homemade.shell;

import homemade.shell.model.settings.Settings;

import javax.swing.*;

/**
 * Created by Marid on 27.03.2016.
 */
public class TestShell
{
    public static void main(String [] args)
    {
        JFrame frame = new JFrame("there's a pattern there to follow");
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parameterTest();

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private static void parameterTest()
    {
        Settings settings = new Settings();

        System.out.println(settings.isRealTime.getValue());
        System.out.println(settings.simultaneousSpawn.getValue());
        System.out.println(settings.spawnPeriod.getValue());

        settings.simultaneousSpawn.setValue(0);
        System.out.println(settings.simultaneousSpawn.getValue());
        settings.simultaneousSpawn.setValue(1);
        System.out.println(settings.simultaneousSpawn.getValue());

        System.out.println();
        System.out.println(settings.something.getValue());
        settings.something.setValue(4);
        System.out.println(settings.something.getValue());
        settings.something.setValue(2);
        System.out.println(settings.something.getValue());
    }
}
