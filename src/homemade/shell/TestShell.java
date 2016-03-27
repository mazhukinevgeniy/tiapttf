package homemade.shell;

import homemade.shell.model.Settings;

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
        System.out.println(Settings.isRealTime.getValue());
        System.out.println(Settings.simultaneousSpawn.getValue());
        System.out.println(Settings.spawnPeriod.getValue());

        Settings.simultaneousSpawn.setValue(0);
        System.out.println(Settings.simultaneousSpawn.getValue());
        Settings.simultaneousSpawn.setValue(1);
        System.out.println(Settings.simultaneousSpawn.getValue());
    }
}
