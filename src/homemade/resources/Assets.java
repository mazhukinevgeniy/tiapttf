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
    private static Assets instance;

    synchronized public static void loadAssets()
    {
        if (instance == null)
            instance = new Assets();
        else
            throw new RuntimeException("no need to load assets again");
    }

    synchronized public static Assets getAssets()
    {
        if (instance == null)
            throw new RuntimeException("must load assets");
        else
            return instance;
    }


    private Image field;
    private Image normalBlock;
    private Image normalBlockSelected;
    private Image deadBlock;
    private Image smallBlock;
    private Image placeToMove;
    private Image digit[];
    private Image disappear[];
    private List<Map<Direction, Image>> arrows;

    private Assets()
    {
        InputStream input;

        digit = new Image[10];
        disappear = new Image[3];

        try
        {
            initializeArrowImages();

            input = getClass().getResourceAsStream("field.png");
            field = ImageIO.read(input);

            input = getClass().getResourceAsStream("normal_block.png");
            normalBlock = ImageIO.read(input);

            input = getClass().getResourceAsStream("normal_block_selected.png");
            normalBlockSelected = ImageIO.read(input);

            input = getClass().getResourceAsStream("gray_block.png");
            deadBlock = ImageIO.read(input);

            input = getClass().getResourceAsStream("small_block.png");
            smallBlock = ImageIO.read(input);

            input = getClass().getResourceAsStream("place2move.png");
            placeToMove = ImageIO.read(input);

            for (int i = 0; i < 10; i++)
            {
                input = getClass().getResourceAsStream(i + ".png");
                digit[i] = ImageIO.read(input);
            }

            for (int i = 0; i < 3; i++)
            {
                input = getClass().getResourceAsStream("dis_" + (i + 1) + ".png");
                disappear[2 - i] = ImageIO.read(input);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Image getField()
    {
        return field;
    }

    public Image getDeadBlock()
    {
        return deadBlock;
    }

    public Image getSmallBlock()
    {
        return smallBlock;
    }

    public Image getBlock(boolean selected)
    {
        return selected ? normalBlockSelected : normalBlock;
    }

    public Image getPlaceToMove()
    {
        return placeToMove;
    }

    public int getNumberOfArrowTiers()
    {
        return arrows.size();
    }

    public Image getArrow(Direction direction, int tier)
    {
        return arrows.get(tier).get(direction);
    }

    public Image getDigit(int value)
    {
        return digit[value];
    }

    public int getDisappearanceLength()
    {
        return disappear.length;
    }

    public Image getDisappearanceSprite(int step)
    {
        return disappear[step];
    }

    private void initializeArrowImages()
    {
        List<String> images = new ArrayList<String>(3);
        images.add("arrow_red.png");
        images.add("arrow_orange.png");
        images.add("arrow_green.png");

        List<Map<Direction, Image>> listOfMaps = new ArrayList<>(3);

        Map<Direction, Double> angles = new EnumMap<>(Direction.class);
        angles.put(Direction.BOTTOM, 0.0);
        angles.put(Direction.LEFT, Math.PI / 2);
        angles.put(Direction.TOP, Math.PI);
        angles.put(Direction.RIGHT, 3 * Math.PI / 2);

        for (String imageName : images)
        {
            Map<Direction, Image> arrows = new EnumMap<>(Direction.class);
            listOfMaps.add(arrows);

            InputStream inputStream = getClass().getResourceAsStream(imageName);
            Image baseImage;

            try
            {
                baseImage = ImageIO.read(inputStream);

                for (Map.Entry<Direction, Double> entry : angles.entrySet())
                {
                    arrows.put(entry.getKey(), createRotatedCopy(baseImage, entry.getValue()));
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        arrows = listOfMaps;
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
