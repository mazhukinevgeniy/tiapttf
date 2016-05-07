package homemade.menu.view.records;

import javax.swing.*;
import java.awt.*;

class RowRecordFactory
{
    private static final int ROW_WIDTH = 200;
    private static final int ROW_HEIGHT = 35;

    public static JPanel create(String place, String playerName, String score)
    {
        JLabel placeLabel = createLabel(place, ROW_WIDTH / 4);
        JLabel playerNameLabel = createLabel(playerName, ROW_WIDTH / 2);
        JLabel scoreLabel = createLabel(score, ROW_WIDTH / 4);

        JPanel rowRecord = new JPanel();
        rowRecord.setLayout(new BoxLayout(rowRecord, BoxLayout.LINE_AXIS));
        rowRecord.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rowRecord.setMaximumSize(new Dimension(ROW_WIDTH, ROW_HEIGHT));

        rowRecord.add(placeLabel);
        rowRecord.add(playerNameLabel);
        rowRecord.add(scoreLabel);

        return rowRecord;
    }

    private static JLabel createLabel(String text, int width)
    {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setMaximumSize(new Dimension(width, ROW_HEIGHT));

        return label;
    }
}
