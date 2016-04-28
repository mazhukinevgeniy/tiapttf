package homemade.resources;

import homemade.resources.links.LinkAssets;
import homemade.utils.AssetLoader;

import java.awt.*;

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

    private LinkAssets linkAssets;

    private Assets()
    {
        digit = new Image[10];
        disappear = new Image[3];

        linkAssets = new LinkAssets(LinkAssets.Variation.COLORED);

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

    public LinkAssets getLinkAssets()
    {
        return linkAssets;
    }
}
