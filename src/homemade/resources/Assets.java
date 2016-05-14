package homemade.resources;

import homemade.resources.blocks.BlockAssets;
import homemade.resources.links.LinkAssets;
import homemade.utils.AssetLoader;

import java.awt.*;

public class Assets extends AssetLoader
{
    private static Assets instance;

    synchronized public static void loadAssets(LinkAssets.Variation variation)
    {
        instance = new Assets(variation);
    }

    synchronized public static Assets getAssets()
    {
        if (instance == null)
            throw new RuntimeException("must load assets");
        else
            return instance;
    }


    private Image field;
    private Image smallBlock;
    private Image placeToMove;
    private Image digit[];
    private Image disappear[];

    private LinkAssets linkAssets;
    private BlockAssets blockAssets;

    private Assets(LinkAssets.Variation variation)
    {
        digit = new Image[10];
        disappear = new Image[3];

        linkAssets = new LinkAssets(variation);
        blockAssets = new BlockAssets();

        field = getImage("field.png");

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

    public Image getSmallBlock()
    {
        return smallBlock;
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

    public BlockAssets getBlockAssets()
    {
        return blockAssets;
    }
}
