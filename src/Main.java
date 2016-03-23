import homemade.game.controller.GameController;
import homemade.resources.Assets;

import javax.swing.*;

/**
 * Created by user3 on 22.03.2016.
 */
public class Main
{
    public static void main(String [] args)
    {
        Assets.loadAssets();

        JFrame frame = new JFrame("there's a pattern there to follow");
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameController controller = new GameController(frame);
        //am I really supposed to pass this reference two constructors deep?
        //TODO: find out




        frame.pack();
        frame.setLocationRelativeTo(null);

    }
}
