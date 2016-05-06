package homemade.game.model.spawn;

import homemade.game.GameSettings;
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
    private static final int MIN_PERIOD = 100;

    static SpawnPeriod newFastStart(GameModelLinker linker, GameSettings settings)
    {
        int minCombo = settings.minCombo();

        int saturationPoint = minCombo * 3;
        int size = linker.getStructure().getFieldSize();
        int oversaturationPoint = size - minCombo * 2;

        if (saturationPoint >= oversaturationPoint)
            throw new RuntimeException("minCombo is too big and fieldSize is too small");

        ArrayList<Integer> separators = new ArrayList<Integer>(4);
        separators.add(saturationPoint / 2);
        separators.add(saturationPoint);
        separators.add((oversaturationPoint + saturationPoint) / 2);
        separators.add(oversaturationPoint);

        int maxPeriod = settings.maxPeriod();
        int spawnsToFill = Math.max(1, (size - oversaturationPoint) / settings.maxSpawn());

        System.out.println("time to fill from oversaturation = " + maxPeriod * spawnsToFill);

        ArrayList<Integer> periods = new ArrayList<Integer>(5);
        periods.add(Math.round(0.2f * maxPeriod));
        periods.add(Math.round(0.3f * maxPeriod));
        periods.add(Math.round(0.5f * maxPeriod));
        periods.add(Math.round(0.8f * maxPeriod));
        periods.add(maxPeriod);

        PiecewiseConstantFunction<Integer, Integer> period =
                new PiecewiseConstantFunction<>(separators, periods);

        return new SpawnPeriod(linker, period);
    }

    private GameModelLinker linker;
    private PiecewiseConstantFunction<Integer, Integer> basePeriod;

    private SpawnPeriod(GameModelLinker linker, PiecewiseConstantFunction<Integer, Integer> period)
    {
        basePeriod = period;

        this.linker = linker;
    }

    long getSpawnPeriod()
    {
        GameState state = linker.lastGameState();

        int occupiedCells = state.numberOfBlocks();
        int spawnsDenied = state.spawnsDenied();


        int base = basePeriod.getValueAt(occupiedCells);
        int decrementFromDenies = 15 * spawnsDenied;

        System.out.println("base period " + base + ", decrement " + decrementFromDenies);

        return Math.max(MIN_PERIOD, base - decrementFromDenies);
    }
}
