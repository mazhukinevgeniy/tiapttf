package homemade.game.view.layers;

import homemade.game.fieldstructure.CellCode;
import homemade.game.view.EffectManager;
import homemade.game.view.ShownEffect;
import homemade.resources.effects.EffectAssets;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

class EffectLayer extends RenderingLayer.Cells
{
    private EffectManager effectManager;

    private Map<ShownEffect, Integer> animationLength;
    private Map<ShownEffect, ArrayList<Image>> sprites;
    private Map<ShownEffect, ArrayList<Offset>> offsets;

    EffectLayer(EffectManager effectManager)
    {
        this.effectManager = effectManager;

        EffectAssets effectAssets = assets.getEffectAssets();

        animationLength = new EnumMap<>(ShownEffect.class);
        animationLength.put(ShownEffect.FADEAWAY, EffectAssets.FADE_SPRITES);
        animationLength.put(ShownEffect.EXPLOSION, EffectAssets.EXPLOSION_SPRITES);

        ArrayList<Image> fadeSprites = new ArrayList<>(EffectAssets.FADE_SPRITES);
        for (int i = 0; i < EffectAssets.FADE_SPRITES; i++)
            fadeSprites.add(i, effectAssets.getFadingBlock(i));

        ArrayList<Image> explosionSprites = new ArrayList<>(EffectAssets.EXPLOSION_SPRITES);
        for (int i = 0; i < EffectAssets.EXPLOSION_SPRITES; i++)
            explosionSprites.add(i, effectAssets.getExplosion(i));

        sprites = new EnumMap<>(ShownEffect.class);
        sprites.put(ShownEffect.FADEAWAY, fadeSprites);
        sprites.put(ShownEffect.EXPLOSION, explosionSprites);

        offsets = new EnumMap<>(ShownEffect.class);
        for (ShownEffect effect : ShownEffect.values())
        {
            int length = animationLength.get(effect);

            ArrayList<Offset> effectOffsets = new ArrayList<>(length);
            ArrayList<Image> effectSprites = sprites.get(effect);

            for (int i = 0; i < length; i++)
                effectOffsets.add(i, new Offset(effectSprites.get(i), 1, 1));

            offsets.put(effect, effectOffsets);
        }
    }

    @Override
    protected void renderForCell(CellCode cellCode)
    {
        for (ShownEffect effect : ShownEffect.values())
        {
            float time = effectManager.getEffectTimeRemaining(cellCode, effect);

            if (time > 0)
            {
                int numberOfFrames = animationLength.get(effect);

                int frame = Math.min(numberOfFrames - 1, Math.round(numberOfFrames * time));

                Image sprite = sprites.get(effect).get(frame);
                Offset offset = offsets.get(effect).get(frame);

                graphics.drawImage(sprite, canvasX + offset.x, canvasY + offset.y, null);
            }
        }
    }
}