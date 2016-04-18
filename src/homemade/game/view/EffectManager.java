package homemade.game.view;

import homemade.game.Effect;
import homemade.game.fieldstructure.CellCode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by user3 on 02.04.2016.
 */
public class EffectManager
{
    private Map<CellCode, Integer> fadingBlocks;

    private long oldTime;

    EffectManager()
    {
        oldTime = System.nanoTime();

        fadingBlocks = new HashMap<>();
    }

    public void displayEffect(Effect effect, CellCode cell)
    {
        int fadeTime = effect.getFullDuration();

        if (effect == Effect.FADING_BLOCK)
        {
            fadingBlocks.put(cell, fadeTime);
        }
        else throw new Error("unknown effect at EffectManager.displayEffect(...)");
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

    public int getFadeTimeRemaining(CellCode cell)
    {
        int time = 0;

        if (fadingBlocks.containsKey(cell))
            time = fadingBlocks.get(cell);

        return time;
    }
}

//TODO: investigate how is this class synchronized