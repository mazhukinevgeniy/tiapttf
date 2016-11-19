package homemade.game.view;

import homemade.game.fieldstructure.CellCode;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EffectManager {
    private static final int BLINK_TIME = 80;

    private int blinkRemaining = 0;
    private boolean greenBlink = true;

    private long oldTime;

    private Map<ShownEffect, EffectTracker> trackers;

    EffectManager() {
        oldTime = System.nanoTime();

        clearEffects();
    }

    synchronized public void addEffect(CellCode cell, ShownEffect shownEffect) {
        trackers.get(shownEffect).addFullEffect(cell);
    }

    synchronized public void blink(boolean isGreen) {
        greenBlink = isGreen;
        blinkRemaining = BLINK_TIME;
    }

    synchronized void clearEffects() {
        int FADE_TIME = 400;
        int EXPLOSION_TIME = 400;

        trackers = new HashMap<>();
        trackers.put(ShownEffect.FADEAWAY, new EffectTracker(FADE_TIME));
        trackers.put(ShownEffect.EXPLOSION, new EffectTracker(EXPLOSION_TIME));

        blinkRemaining = 0;
    }

    synchronized void measureTimePassed() {
        long newTime = System.nanoTime();
        long difference = newTime - oldTime;

        long nanosecondsPerMillisecond = 1000 * 1000;

        oldTime = newTime;
        updateEffects((int) (difference / nanosecondsPerMillisecond));
    }

    private synchronized void updateEffects(int differenceMS) {
        blinkRemaining = Math.max(0, blinkRemaining - differenceMS);

        for (Map.Entry<ShownEffect, EffectTracker> entry : trackers.entrySet())
            entry.getValue().updateEffects(differenceMS);
    }

    synchronized public float getEffectTimeRemaining(CellCode cell, ShownEffect effect) {
        return trackers.get(effect).getTimeRemaining(cell);
    }

    synchronized Color getBackgroundColor() {
        Color base = Color.LIGHT_GRAY;
        float shift = (float) blinkRemaining / BLINK_TIME;
        Color farEnd = greenBlink ? Color.GREEN : Color.RED;

        int newR = base.getRed() + (int) (shift * farEnd.getRed());
        int newG = base.getGreen() + (int) (shift * farEnd.getGreen());
        int newB = base.getBlue() + (int) (shift * farEnd.getBlue());

        int max = Math.max(Math.max(newR, newG), newB);

        return new Color(newR * 255 / max, newG * 255 / max, newB * 255 / max);
    }

    private static class EffectTracker {
        private Map<CellCode, Integer> effects;

        private int fullTime;


        private EffectTracker(int fullTime) {
            this.fullTime = fullTime;

            effects = new HashMap<>();
        }

        private void addFullEffect(CellCode cell) {
            effects.put(cell, fullTime);
        }

        private float getTimeRemaining(CellCode cell) {
            float time = 0;

            if (effects.containsKey(cell))
                time = (float) effects.get(cell) / fullTime;

            return time;
        }

        private void updateEffects(int differenceMS) {
            Iterator<Map.Entry<CellCode, Integer>> iterator = effects.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<CellCode, Integer> entry = iterator.next();

                int oldTime = entry.getValue();
                int newTime = oldTime - differenceMS;

                if (newTime < 0)
                    iterator.remove();
                else
                    effects.replace(entry.getKey(), oldTime, newTime);
            }
        }
    }
}