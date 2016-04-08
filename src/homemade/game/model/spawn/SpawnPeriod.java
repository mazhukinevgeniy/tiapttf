package homemade.game.model.spawn;

import homemade.game.Game;
import homemade.game.model.GameStats;
import homemade.utils.PiecewiseConstantFunction;

import java.util.ArrayList;

/**
 * Let's say field is empty if you can't make a combo,
 * field is saturated if it's not empty and there's plenty of blocks and some free space,
 * field is oversaturated if block percentage is above a certain threshold
 * and field is full if there's no free space.
 *
 * The desirable period is like that:
 * 1) It's little when field is empty;
 * 2) It's manageable when field is saturated, ie you can set up huge combos in time;
 * 3) It will fill the field in the desirable time after the point of oversaturation.
 *
 * Plus, it goes down a bit every time you deny a spawn.
 */
class SpawnPeriod
{
    private static final int SATURATION_POINT = Game.MIN_COMBO * 3;
    private static final int OVERSATURATION_POINT = Game.FIELD_SIZE - Game.MIN_COMBO;
    private static final int TIME_TO_FILL = 10 * 1000;

    private GameStats stats;

    private PiecewiseConstantFunction<Integer, Integer> basePeriod;

    SpawnPeriod(GameStats stats)
    {
        this(stats, SATURATION_POINT, OVERSATURATION_POINT, TIME_TO_FILL);
    }

    SpawnPeriod(GameStats stats, int saturationPoint, int oversaturationPoint, int timeToFill)
    {
        ArrayList<Integer> separators = new ArrayList<Integer>(2);
        separators.add(saturationPoint);
        separators.add(oversaturationPoint);

        int startPeriod = 250;

        int spawnsToFillFromOversaturation = 1 + (Game.FIELD_SIZE - oversaturationPoint) / Game.SIMULTANEOUS_SPAWN;
        int finalPeriod = timeToFill / spawnsToFillFromOversaturation;

        ArrayList<Integer> periods = new ArrayList<Integer>(3);
        periods.add(startPeriod);
        periods.add(finalPeriod * 2);
        periods.add(finalPeriod);

        basePeriod = new PiecewiseConstantFunction<Integer, Integer>(separators, periods);

        this.stats = stats;

        if (saturationPoint >= oversaturationPoint || oversaturationPoint >= Game.FIELD_SIZE)
            throw new Error("SpawnPeriod initialized incorrectly");
    }

    long getSpawnPeriod()
    {
        int occupiedCells = stats.getCellsOccupied();

        int base = basePeriod.getValueAt(occupiedCells);

        return base;//TODO: add modifier from denied spawns
    }

}
