package homemade.menu.view.mainMenu;

import homemade.menu.controller.ButtonActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class MainMenuButtonFactory
{
    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);
    private static final int WIDTH = 200;
    private static final int HEIGHT = 30;

    private static final int SIZE_BORDER = 10;

    public static JButton createButton(String caption, ButtonActionListener actionListener)
    {
        JButton button = new JButton(caption);

        button.setMaximumSize(new Dimension(WIDTH + 2 * SIZE_BORDER, HEIGHT + 2 * SIZE_BORDER));
        button.setFont(FONT);
        button.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        Border outsideBorder = new EmptyBorder(10, 10, 10, 10);
        Border insideBorder = button.getBorder();
        button.setBorder(new CompoundBorder(outsideBorder, insideBorder));

        button.addActionListener(actionListener);

        return button;
    }
}
