package homemade.game.view.layers;

import homemade.game.Effect;
import homemade.game.view.GameView;
import homemade.resources.Assets;

import java.awt.*;

/**
 * Created by user3 on 02.04.2016.
 */
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

            int frame = (numberOfFrames * timeRemaining) / Effect.FADING_BLOCK.getFullDuration();
            System.out.println("frame " + frame);

            Image sprite = Assets.disappear[frame];

            int offsetX = (GameView.CellWidth - sprite.getWidth(null)) / 2;
            int offsetY = (GameView.CellWidth - sprite.getHeight(null)) / 2;

            graphics.drawImage(sprite, canvasX + offsetX, canvasY + offsetY, null);
        }
    }
}
