package homemade.menu.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marid on 02.04.2016.
 */
public class MainMenu extends Menu
{
    public static Font font = new Font("Verdana", Font.PLAIN, 13);

    public MainMenu()
    {
        super();

        setLayout(new BorderLayout());
        JButton gameButton = new JButton("Game");
        gameButton.setFont(font);
        add(gameButton, BorderLayout.NORTH);
    }
}
