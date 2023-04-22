package homemade.resources.blocks;

import homemade.game.model.combo.ComboEffect;
import homemade.utils.AssetLoader;

import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class BlockAssets extends AssetLoader {
    private static enum SelectionType {
        NOT_SELECTED, SELECTED, NOT_SELECTABLE;
    }

    private Map<SelectionType, Map<ComboEffect, Image>> assets;

    private Image deadBlock;

    public BlockAssets() {
        assets = new EnumMap<>(SelectionType.class);

        String[] baseSprites = new String[3];
        baseSprites[SelectionType.SELECTED.ordinal()] = "normal_block_selected.png";
        baseSprites[SelectionType.NOT_SELECTED.ordinal()] = "normal_block.png";
        baseSprites[SelectionType.NOT_SELECTABLE.ordinal()] = "immovable_block.png";

        EnumMap<ComboEffect, String> comboSprites = new EnumMap<>(ComboEffect.class);
        comboSprites.put(ComboEffect.EXTRA_MULTIPLIER, "tria.png");
        comboSprites.put(ComboEffect.JUST_EXTRA_TIER, "square.png");
        comboSprites.put(ComboEffect.EXPLOSION, "expl.png");

        for (SelectionType type : SelectionType.values()) {
            Map<ComboEffect, Image> map = new HashMap<>();
            assets.put(type, map);

            String base = baseSprites[type.ordinal()];
            map.put(ComboEffect.UNDEFINED_COMBO_EFFECT, getImage(base));

            for (ComboEffect effect : ComboEffect.values()) {
                if (effect == ComboEffect.UNDEFINED_COMBO_EFFECT) {
                    continue;
                }
                map.put(effect, stackSprites(new Image[]{
                        getImage(base),
                        getImage(comboSprites.get(effect))
                }));
            }
        }

        deadBlock = getImage("gray_block.png");
    }

    public Image getDeadBlock() {
        return deadBlock;
    }

    public Image getBlock(boolean movable, boolean selected, ComboEffect effect) {
        return assets.get(getSelectionType(movable, selected)).get(effect);
    }

    private SelectionType getSelectionType(boolean movable, boolean selected) {
        if (movable && selected) {
            return SelectionType.SELECTED;
        } else if (movable) {
            return SelectionType.NOT_SELECTED;
        } else if (!selected) {
            return SelectionType.NOT_SELECTABLE;
        } else {
            throw new IllegalArgumentException("if block is not movable it must not be selected");
        }
    }
}
