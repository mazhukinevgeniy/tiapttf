package homemade.resources;

import homemade.resources.blocks.BlockAssets;
import homemade.resources.effects.EffectAssets;
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

    private BlockAssets blockAssets;
    private EffectAssets effectAssets;
    private LinkAssets linkAssets;

    private Assets(LinkAssets.Variation variation)
    {
        digit = new Image[10];

        blockAssets = new BlockAssets();
        effectAssets = new EffectAssets();
        linkAssets = new LinkAssets(variation);

        field = getImage("field.png");

        smallBlock = getImage("small_block.png");
        placeToMove = getImage("place2move.png");

        for (int i = 0; i < 10; i++)
        {
            digit[i] = getImage(i + ".png");
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

    public LinkAssets getLinkAssets()
    {
        return linkAssets;
    }

    public BlockAssets getBlockAssets()
    {
        return blockAssets;
    }

    public EffectAssets getEffectAssets()
    {
        return effectAssets;
    }
}
