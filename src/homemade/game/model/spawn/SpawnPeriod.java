package homemade.game.model.spawn;

import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.model.GameModelLinker;
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
    private static final int TIME_TO_FILL = 6 * 1000;

    private static final int MIN_PERIOD = 100;

    private GameModelLinker linker;

    private PiecewiseConstantFunction<Integer, Integer> basePeriod;

    SpawnPeriod(GameModelLinker linker)
    {
        this(linker, SATURATION_POINT, OVERSATURATION_POINT, TIME_TO_FILL);
    }

    SpawnPeriod(GameModelLinker linker, int saturationPoint, int oversaturationPoint, int timeToFill)
    {
        ArrayList<Integer> separators = new ArrayList<Integer>(4);
        separators.add(saturationPoint / 2);
        separators.add(saturationPoint);
        separators.add((oversaturationPoint + saturationPoint) / 2);
        separators.add(oversaturationPoint);


        int spawnsToFill = Math.max(1, (Game.FIELD_SIZE - oversaturationPoint) / SpawnManager.SIMULTANEOUS_SPAWN);
        int finalPeriod = timeToFill / spawnsToFill;

        ArrayList<Integer> periods = new ArrayList<Integer>(5);
        periods.add(Math.round(0.2f * finalPeriod));
        periods.add(Math.round(0.3f * finalPeriod));
        periods.add(Math.round(0.5f * finalPeriod));
        periods.add(Math.round(0.8f * finalPeriod));
        periods.add(finalPeriod);

        basePeriod = new PiecewiseConstantFunction<Integer, Integer>(separators, periods);

        this.linker = linker;

        if (saturationPoint >= oversaturationPoint || oversaturationPoint >= Game.FIELD_SIZE)
            throw new Error("SpawnPeriod initialized incorrectly");
    }

    long getSpawnPeriod()
    {
        GameState state = linker.copyGameState();

        int occupiedCells = state.getCellsOccupied();
        int spawnsDenied = state.getSpawnsDenied();


        int base = basePeriod.getValueAt(occupiedCells);
        int decrementFromDenies = 15 * spawnsDenied;

        System.out.println("base period " + base + ", decrement " + decrementFromDenies);

        return Math.max(MIN_PERIOD, base - decrementFromDenies);
    }

}
