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
        selectedBlocks.put(ComboEffect.JUST_EXTRA_TIER, getImage("special_block_selected.png"));
        selectedBlocks.put(ComboEffect.EXPLOSION, getImage("normal_block_selected.png"));

        unselectedBlocks.put(null, getImage("normal_block.png"));
        unselectedBlocks.put(ComboEffect.JUST_EXTRA_TIER, getImage("special_block.png"));
        unselectedBlocks.put(ComboEffect.EXPLOSION, getImage("normal_block.png"));//TODO: get more sprites
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
