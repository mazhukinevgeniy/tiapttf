package homemade.game.view;

import homemade.game.Effect;
import homemade.game.fieldstructure.CellCode;
import homemade.utils.QuickMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

        fadingBlocks = QuickMap.getCleanCellCodeIntMap();
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


        Set<CellCode> keys = fadingBlocks.keySet();
        Iterator<CellCode> iterator = keys.iterator();

        while (iterator.hasNext())
        {
            CellCode key = iterator.next();

            int oldTime = fadingBlocks.get(key);
            int newTime = oldTime - differenceMS;

            if (newTime < 0)
            {
                iterator.remove();
            }
            else
            {
                fadingBlocks.replace(key, oldTime, newTime);
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