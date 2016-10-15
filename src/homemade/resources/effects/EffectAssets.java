package homemade.resources.effects;

import homemade.utils.AssetLoader;

import java.awt.*;

public class EffectAssets extends AssetLoader
{
    public static final int FADE_SPRITES = 3, EXPLOSION_SPRITES = 6;

    private Image fade[], explosion[];

    public EffectAssets()
    {
        fade = new Image[FADE_SPRITES];
        explosion = new Image[EXPLOSION_SPRITES];

        for (int i = 1; i < FADE_SPRITES + 1; i++)
            fade[3 - i] = getImage("dis_" + i + ".png");

        for (int i = 1; i < EXPLOSION_SPRITES + 1; i++)
            explosion[i - 1] = getImage("stone_boom_" + i + ".png");
    }

    public Image getFadingBlock(int stage)
    {
        return fade[stage];
    }

    public Image getExplosion(int stage)
    {
        return explosion[stage];
    }
}
