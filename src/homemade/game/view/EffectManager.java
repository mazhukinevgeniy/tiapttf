package homemade.game.view;

import homemade.game.Effect;
import homemade.game.fieldstructure.CellCode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EffectManager
{
    private Map<CellCode, Integer> fadingBlocks;

    private long oldTime;

    EffectManager()
    {
        oldTime = System.nanoTime();

        fadingBlocks = new HashMap<>();
    }

    synchronized public void displayEffect(Effect effect, CellCode cell)
    {
        int fadeTime = getDuration(effect);

        if (effect == Effect.FADING_BLOCK)
        {
            fadingBlocks.put(cell, fadeTime);
        }
        else
            throw new RuntimeException("unknown effect at EffectManager.displayEffect(...)");
    }

    synchronized public void clearEffects()
    {
        fadingBlocks = new HashMap<>();
    }

    void measureTimePassed()
    {
        long newTime = System.nanoTime();
        long difference = newTime - oldTime;

        long nanosecondsPerMillisecond = 1000 * 1000;

        oldTime = newTime;
        updateEffects((int)(difference / nanosecondsPerMillisecond));

    }

    private synchronized void updateEffects(int differenceMS)
    {

        Iterator<Map.Entry<CellCode, Integer>> iterator = fadingBlocks.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<CellCode, Integer> entry = iterator.next();

            int oldTime = entry.getValue();
            int newTime = oldTime - differenceMS;

            if (newTime < 0)
            {
                iterator.remove();
            }
            else
            {
                fadingBlocks.replace(entry.getKey(), oldTime, newTime);
            }
        }
    }

    synchronized public float getFadeTimeRemaining(CellCode cell)
    {
        float time = 0;

        if (fadingBlocks.containsKey(cell))
            time = (float)fadingBlocks.get(cell) / getDuration(Effect.FADING_BLOCK);

        return time;
    }

    private int getDuration(Effect effect)
    {
        return 400;
    }
}