package homemade.menu.view.records;

import homemade.menu.controller.ButtonActionListener;

import javax.swing.*;
import java.awt.*;

class RecordsButtonFactory
{
    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);
    private static final int WIDTH = 100;
    private static final int HEIGHT = 30;

    public static JButton createButton(String caption, ButtonActionListener actionListener)
    {
        JButton button = new JButton(caption);

        button.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        button.setFont(FONT);
        button.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        button.addActionListener(actionListener);

        return button;
    }
}
