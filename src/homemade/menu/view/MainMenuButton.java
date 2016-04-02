package homemade.menu.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Marid on 02.04.2016.
 */
public class MainMenuButton extends JButton
{
    private static Font font = new Font("Verdana", Font.PLAIN, 13);
    private static int WIDTH = 200;
    private static int HEIGHT = 30;

    private static int SIZE_BORDER = 10;

    public MainMenuButton(String caption)
    {
        super(caption);

        setMaximumSize(new Dimension(WIDTH + 2 * SIZE_BORDER, HEIGHT + 2 * SIZE_BORDER));
        setFont(font);
        setAlignmentX(JComponent.CENTER_ALIGNMENT);

        Border outsideBorder = new EmptyBorder(10, 10, 10, 10);
        Border insideBorder = getBorder();
        setBorder(new CompoundBorder(outsideBorder, insideBorder));
    }

}
