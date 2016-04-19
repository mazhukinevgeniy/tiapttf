package homemade.menu.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marid on 02.04.2016.
 */
public class Window extends JFrame
{
    public static final String CAPTION = "there's a pattern there to follow";
    public static final int WIDTH = 480;
    public static final int HEIGHT = 640;

    public Window()
    {
        super(CAPTION);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    @Override
    public Component add(Component component)
    {
        return getContentPane().add(component);
    }

    @Override
    public void remove(Component component)
    {
        getContentPane().remove(component);
    }

    public void finaleInitialize()
    {
        pack();
        setLocationRelativeTo(null);
    }
}
