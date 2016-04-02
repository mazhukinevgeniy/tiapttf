package homemade.resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user3 on 23.03.2016.
 */
public class Assets
{
    public static Image grid;
    public static Image field;
    public static Image glowVertical;
    public static Image glowHorizontal;
    public static Image normalBlock;
    public static Image normalBlockSelected;
    public static Image smallBlock;
    public static Image placeToMove;
    public static Image digit[];
    public static Image disappear[];

    public static void loadAssets()
    {
        new Assets();
    }

    private Assets()
    {
        InputStream input;

        Assets.digit = new Image[10];
        Assets.disappear = new Image[3];


        try
        {
            input = getClass().getResourceAsStream("grid.png");
            Assets.grid = ImageIO.read(input);

            input = getClass().getResourceAsStream("field.png");
            Assets.field = ImageIO.read(input);

            input = getClass().getResourceAsStream("glow.png");
            Assets.glowVertical = ImageIO.read(input);
            Assets.glowHorizontal = createRotatedCopy(Assets.glowVertical, Math.PI / 2);

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

            for (int i = 0; i < 3; i++)
            {
                input = getClass().getResourceAsStream("dis_" + (i + 1) + ".png");
                Assets.disappear[2 - i] = ImageIO.read(input);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private Image createRotatedCopy(Image image, double angleInRadians)
    {
        int     width = image.getWidth(null),
                height = image.getHeight(null);


        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        //buffered image is ready

        double  sin = Math.abs(Math.sin(angleInRadians)),
                cos = Math.abs(Math.cos(angleInRadians));


        int     newW = (int) Math.floor(width * cos + height * sin),
                newH = (int) Math.floor(height * cos + width * sin);

        BufferedImage rotatedImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        graphics = rotatedImage.createGraphics();

        graphics.translate((newW - width) / 2, (newH - height) / 2);
        graphics.rotate(angleInRadians, width / 2, height / 2);
        graphics.drawRenderedImage(bufferedImage, null);
        graphics.dispose();

        return rotatedImage;
    }
}
