package homemade.game.view.layers;

import homemade.game.Effect;
import homemade.game.view.GameView;
import homemade.resources.Assets;

import java.awt.*;

class EffectRenderer
{

    EffectRenderer()
    {
        super();
    }

    void renderFadingBlock(int canvasX, int canvasY, int timeRemaining, Graphics graphics)
    {
        if (timeRemaining > 0)
        {
            int numberOfFrames = Assets.disappear.length;

            int frame = Math.min(numberOfFrames - 1,
                    (numberOfFrames * timeRemaining) / Effect.FADING_BLOCK.getFullDuration());

            Image sprite = Assets.disappear[frame];

            int offsetX = (GameView.CELL_WIDTH - sprite.getWidth(null)) / 2;
            int offsetY = (GameView.CELL_WIDTH - sprite.getHeight(null)) / 2;

            graphics.drawImage(sprite, canvasX + offsetX, canvasY + offsetY, null);
        }
    }
}