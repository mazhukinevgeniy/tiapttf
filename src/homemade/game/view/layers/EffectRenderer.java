package homemade.game.view.layers;

import homemade.game.view.GameView;
import homemade.resources.Assets;
import homemade.resources.effects.EffectAssets;

import java.awt.*;

class EffectRenderer
{
    private EffectAssets assets;

    EffectRenderer()
    {
        assets = Assets.getAssets().getEffectAssets();
    }

    void renderFadingBlock(int canvasX, int canvasY, float timeRemaining, Graphics graphics)
    {
        if (timeRemaining > 0)
        {
            int numberOfFrames = EffectAssets.FADE_SPRITES;

            int frame = Math.min(numberOfFrames - 1,
                                 Math.round(numberOfFrames * timeRemaining));

            Image sprite = assets.getFadingBlock(frame);

            int offsetX = (GameView.CELL_WIDTH - sprite.getWidth(null)) / 2;
            int offsetY = (GameView.CELL_WIDTH - sprite.getHeight(null)) / 2;

            graphics.drawImage(sprite, canvasX + offsetX, canvasY + offsetY, null);
        }
    }
}