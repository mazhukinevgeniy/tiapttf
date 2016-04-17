package homemade.menu.view.mainMenu;

import homemade.menu.controller.ButtonActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Marid on 02.04.2016.
 */
class MainMenuButton extends JButton
{
    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);
    private static final int WIDTH = 200;
    private static final int HEIGHT = 30;

    private static final int SIZE_BORDER = 10;

    public MainMenuButton(String caption, ButtonActionListener actionListener)
    {
        super(caption);

        setMaximumSize(new Dimension(WIDTH + 2 * SIZE_BORDER, HEIGHT + 2 * SIZE_BORDER));
        setFont(FONT);
        setAlignmentX(JComponent.CENTER_ALIGNMENT);

        Border outsideBorder = new EmptyBorder(10, 10, 10, 10);
        Border insideBorder = getBorder();
        setBorder(new CompoundBorder(outsideBorder, insideBorder));

        addActionListener(actionListener);
    }



}
