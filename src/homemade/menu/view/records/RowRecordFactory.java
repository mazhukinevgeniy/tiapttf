package homemade.menu.view.records;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marid on 01.05.2016.
 */
class RowRecordFactory
{
    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);
    private static final String SEPARATOR = "       ";

    public static JLabel create(String place, String playerName, String score)
    {
        JLabel label = new JLabel(place + SEPARATOR + playerName + SEPARATOR + score);
        label.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        return label;
    }
}
