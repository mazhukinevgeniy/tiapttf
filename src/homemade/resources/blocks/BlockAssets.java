package homemade.resources.blocks;

import homemade.game.Cell.ComboEffect;
import homemade.utils.AssetLoader;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BlockAssets extends AssetLoader
{
    private Map<Boolean, Map<ComboEffect, Image>> assets;

    private Image deadBlock;

    public BlockAssets()
    {
        assets = new HashMap<>();

        Map<ComboEffect, Image> selectedBlocks = new HashMap<>();
        Map<ComboEffect, Image> unselectedBlocks = new HashMap<>();

        assets.put(true, selectedBlocks);
        assets.put(false, unselectedBlocks);

        selectedBlocks.put(null, getImage("normal_block_selected.png"));
        selectedBlocks.put(ComboEffect.EXTRA_MULTIPLIER, stackSprites(new Image[]
                {getImage("normal_block_selected.png"), getImage("tria.png")}));
        selectedBlocks.put(ComboEffect.JUST_EXTRA_TIER, stackSprites(new Image[]
                {getImage("normal_block_selected.png"), getImage("square.png")}));
        selectedBlocks.put(ComboEffect.EXPLOSION, stackSprites(new Image[]
                {getImage("normal_block_selected.png"), getImage("expl.png")}));

        unselectedBlocks.put(null, getImage("normal_block.png"));
        unselectedBlocks.put(ComboEffect.EXTRA_MULTIPLIER, stackSprites(new Image[]
                {getImage("normal_block.png"), getImage("tria.png")}));
        unselectedBlocks.put(ComboEffect.JUST_EXTRA_TIER, stackSprites(new Image[]
                {getImage("normal_block.png"), getImage("square.png")}));
        unselectedBlocks.put(ComboEffect.EXPLOSION, stackSprites(new Image[]
                {getImage("normal_block.png"), getImage("expl.png")}));
        unselectedBlocks.put(ComboEffect.IMMOVABLE, getImage("immovable_block.png"));

        deadBlock = getImage("gray_block.png");
    }

    public Image getDeadBlock()
    {
        return deadBlock;
    }

    public Image getBlock(boolean selected, ComboEffect effect)
    {
        return assets.get(selected).get(effect);
    }

}