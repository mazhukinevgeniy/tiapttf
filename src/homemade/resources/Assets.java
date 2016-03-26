package homemade.resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user3 on 23.03.2016.
 */
public class Assets
{
    public static Image grid;
    public static Image field;
    public static Image normalBlock;
    public static Image normalBlockSelected;
    public static Image smallBlock;
    public static Image placeToMove;
    public static Image digit[];

    public static void loadAssets()
    {
        new Assets();
    }

    private Assets()
    {
        InputStream input;

        Assets.digit = new Image[10];


        try
        {
            input = getClass().getResourceAsStream("grid.png");
            Assets.grid = ImageIO.read(input);

            input = getClass().getResourceAsStream("field.png");
            Assets.field = ImageIO.read(input);

            input = getClass().getResourceAsStream("normal_block.png");
            Assets.normalBlock = ImageIO.read(input);

            input = getClass().getResourceAsStream("normal_block_selected.png");
            Assets.normalBlockSelected = ImageIO.read(input);

            input = getClass().getResourceAsStream("small_block.png");
            Assets.smallBlock = ImageIO.read(input);

            input = getClass().getResourceAsStream("place2move.png");
            Assets.placeToMove = ImageIO.read(input);

            for (int i = 0; i < 10; i++)
            {
                input = getClass().getResourceAsStream(i + ".png");
                Assets.digit[i] = ImageIO.read(input);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
