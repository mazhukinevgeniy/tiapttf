package homemade.game.model.spawn;

import homemade.game.Game;

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
    private static final double SATURATION_POINT = 0.4;
    private static final double OVERSATURATION_POINT = 0.9;
    private static final int TIME_TO_FILL = 10 * 1000;

    private double saturationPoint;
    private double oversaturationPoint;
    private int timeToFill;

    private int spawnsDenied = 0;

    SpawnPeriod()
    {
        this(SATURATION_POINT, OVERSATURATION_POINT, TIME_TO_FILL);
    }

    SpawnPeriod(double saturationPoint, double oversaturationPoint, int timeToFill)
    {
        this.saturationPoint = saturationPoint;
        this.oversaturationPoint = oversaturationPoint;
        this.timeToFill = timeToFill;


    }

    void spawnDenied()
    {
        spawnsDenied++;
    }

    long getSpawnPeriod()
    {
        long basePeriod = Game.SPAWN_PERIOD;//TODO: implement variable period

        //double occupiedCellsPercentage =

        return basePeriod;
    }

}
