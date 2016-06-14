package homemade.game.view;

import homemade.game.fieldstructure.CellCode;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EffectManager
{
    private static final int FADE_TIME = 400;
    private static final int BLINK_TIME = 80;

    private Map<CellCode, Integer> fadingBlocks;

    private int blinkRemaining = 0;
    private boolean greenBlink = true;

    private long oldTime;

    EffectManager()
    {
        oldTime = System.nanoTime();

        fadingBlocks = new HashMap<>();
    }

    synchronized public void addFadingBlock(CellCode cell)
    {
        fadingBlocks.put(cell, FADE_TIME);
    }

    synchronized public void blink(boolean isGreen)
    {
        greenBlink = isGreen;
        blinkRemaining = BLINK_TIME;
    }

    synchronized void clearEffects()
    {
        fadingBlocks = new HashMap<>();
        blinkRemaining = 0;
    }

    synchronized void measureTimePassed()
    {
        long newTime = System.nanoTime();
        long difference = newTime - oldTime;

        long nanosecondsPerMillisecond = 1000 * 1000;

        oldTime = newTime;
        updateEffects((int)(difference / nanosecondsPerMillisecond));
    }

    private synchronized void updateEffects(int differenceMS)
    {
        blinkRemaining = Math.max(0, blinkRemaining - differenceMS);

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
            time = (float)fadingBlocks.get(cell) / FADE_TIME;

        return time;
    }

    synchronized Color getBackgroundColor()
    {
        Color base = Color.LIGHT_GRAY;
        float shift = (float)blinkRemaining / BLINK_TIME;
        Color farEnd = greenBlink ? Color.GREEN : Color.RED;

        int newR = base.getRed() + (int)(shift * farEnd.getRed());
        int newG = base.getGreen() + (int)(shift * farEnd.getGreen());
        int newB = base.getBlue() + (int)(shift * farEnd.getBlue());

        int max = Math.max(Math.max(newR, newG), newB);

        return new Color(newR * 255 / max, newG * 255 / max, newB * 255 / max);
    }
}