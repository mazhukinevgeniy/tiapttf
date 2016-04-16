package homemade.resources;

import homemade.game.fieldstructure.Direction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
    public static Image disappear[];
    public static List<Map<Direction, Image>> arrows;

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
            initializeArrowImages();

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

    private void initializeArrowImages()
    {
        List<String> images = new ArrayList<String>(3);
        images.add("arrow_red.png");
        images.add("arrow_orange.png");
        images.add("arrow_green.png");

        List<Map<Direction, Image>> listOfMaps = new ArrayList<>(3);

        Map<Direction, Double> angles = new EnumMap<>(Direction.class);
        angles.put(Direction.TOP, 0.0);
        angles.put(Direction.LEFT, 3 * Math.PI / 2);
        angles.put(Direction.BOTTOM, Math.PI);
        angles.put(Direction.RIGHT, Math.PI / 2);

        for (String imageName : images)
        {
            Map<Direction, Image> arrows = new EnumMap<>(Direction.class);
            listOfMaps.add(arrows);

            InputStream inputStream = getClass().getResourceAsStream(imageName);
            Image baseImage;

            try
            {
                baseImage = ImageIO.read(inputStream);

                for (Direction direction : angles.keySet())
                {
                    arrows.put(direction, createRotatedCopy(baseImage, angles.get(direction)));
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        Assets.arrows = listOfMaps;
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
