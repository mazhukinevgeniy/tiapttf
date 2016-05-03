package homemade.menu.view.records;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marid on 01.05.2016.
 */
class RowRecordFactory
{
    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);
    private static final int WIDTH = 200;
    private static final String SEPARATOR = "       ";

    public static JPanel create(String place, String playerName, String score)
    {
        JLabel placeLabel = new JLabel(place);
        JLabel playerNameLabel = new JLabel(playerName);
        JLabel scoreLabel = new JLabel(score);

        placeLabel.setMaximumSize(new Dimension(WIDTH / 4, 30));
        placeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        playerNameLabel.setMaximumSize(new Dimension(WIDTH / 2, 30));
        playerNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        scoreLabel.setMaximumSize(new Dimension(WIDTH / 4, 30));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //playerNameLabel.setMaximumSize(new Dimension(WIDTH / 2, 30));
        //playerNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //scoreLabel.setMaximumSize(new Dimension(100, 30));
        //scoreLabel.setMinimumSize(new Dimension(WIDTH, 100));
        //scoreLabel.set

        //placeLabel.setHorizontalAlignment(SwingConstants.LEFT);//
        //placeLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        //playerNameLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        //scoreLabel.setAlignmentX(JComponent.RIGHT_ALIGNMENT);


        JPanel rowRecord = new JPanel();
        //rowRecord.setLayout(new GridLayout(0, 1));
        rowRecord.setLayout(new BoxLayout(rowRecord, BoxLayout.LINE_AXIS));
        //rowRecord.setLayout(new BoxLayout(rowRecord, BoxLayout.LINE_AXIS));
        rowRecord.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //rowRecord.setLayout(new BorderLayout(WIDTH / 6, 0));
        //rowRecord.setAlignmentX(Component.CENTER_ALIGNMENT);
        //rowRecord.setMinimumSize(new Dimension(WIDTH, 100));
        rowRecord.setMaximumSize(new Dimension(WIDTH, 30));
        rowRecord.setBackground(Color.CYAN);

        //rowRecord.add(Box.createHorizontalGlue());
        //rowRecord.add(Box.createGlue());
        rowRecord.add(placeLabel);
        //rowRecord.add(Box.createGlue());
        //rowRecord.add(Box.createHorizontalGlue());

            /*JPanel box = new JPanel();
            box.setMaximumSize(new Dimension(WIDTH / 2, 60));
            //box.setMinimumSize(new Dimension(WIDTH / 2, 30));
            //box.setBackground(Color.BLUE);
            //box.add(Box.createGlue());
            box.add(playerNameLabel);*/
            //playerNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            //box.add(Box.createGlue());

        //rowRecord.add(playerNameLabel);
        //playerNameLabel.setMaximumSize(new Dimension(WIDTH / 2, 30));
        rowRecord.add(playerNameLabel);
        //rowRecord.add(Box.createGlue());
        rowRecord.add(scoreLabel);
        //rowRecord.add(Box.createGlue());

        /*Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.setMaximumSize(new Dimension(WIDTH, 30));
        horizontalBox.add(new JButton("Left"));
        horizontalBox.add(Box.createGlue());
        horizontalBox.add(new JButton("Middle"));
        horizontalBox.add(Box.createGlue());
        horizontalBox.add(new JButton("Right"));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(horizontalBox);
        panel.setBorder(BorderFactory.createTitledBorder("2 Middle Glues"));
        rowRecord.add(panel);*/
        //placeLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        //playerNameLabel.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        //rowRecord.add(scoreLabel);

        return rowRecord;
    }
}
