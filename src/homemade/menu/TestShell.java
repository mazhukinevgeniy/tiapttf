package homemade.menu;


import homemade.menu.model.save.Save;
import homemade.menu.model.settings.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marid on 27.03.2016.
 */
public class TestShell
{
    public static void main(String [] args)  throws Exception
    {
        JFrame frame = new JFrame("there's a pattern there to follow");
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createGUI();

        Save save = new Save("settings.xml");
        parameterTest(save);

        frame.pack();
        frame.setLocationRelativeTo(null);
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
        final JFrame frame = new JFrame("Test frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Font font = new Font("Verdana", Font.PLAIN, 13);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        final JTextArea textArea = new JTextArea(15, 10);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        textArea.setFont(font);

        JButton parseButton = new JButton("Parse XML");
        parseButton.setFont(font);
        panel.add(parseButton, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);

        frame.setPreferredSize(new Dimension(280, 220));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
