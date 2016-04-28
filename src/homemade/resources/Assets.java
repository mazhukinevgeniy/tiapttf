package homemade.resources;

import homemade.game.fieldstructure.Direction;
import homemade.utils.AssetLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Assets extends AssetLoader
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
        digit = new Image[10];
        disappear = new Image[3];

        initializeArrowImages();

        field = getImage("field.png");

        normalBlock = getImage("normal_block.png");
        normalBlockSelected = getImage("normal_block_selected.png");

        deadBlock = getImage("gray_block.png");
        smallBlock = getImage("small_block.png");

        placeToMove = getImage("place2move.png");

        for (int i = 0; i < 10; i++)
        {
            digit[i] = getImage(i + ".png");
        }

        for (int i = 0; i < 3; i++)
        {
            disappear[2 - i] = getImage("dis_" + (i + 1) + ".png");
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

    public int getNumberOfArrowFrames()
    {
        return 1; //TODO: return actual value
    }

    public Image getArrow(Direction direction, int tier, int frame)
    {
        return arrows.get(tier).get(direction);//TODO: use frame
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

            Image baseImage = getImage(imageName);

            for (Map.Entry<Direction, Double> entry : angles.entrySet())
            {
                arrows.put(entry.getKey(), createRotatedCopy(baseImage, entry.getValue()));
            }
        }

        arrows = listOfMaps;
    }
}
