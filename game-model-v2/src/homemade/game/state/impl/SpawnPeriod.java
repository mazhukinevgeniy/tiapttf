package homemade.game.state.impl;

import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.GameSettings;
import homemade.game.state.ConfigState;
import homemade.game.state.FieldState;
import homemade.util.PiecewiseConstantFunction;

import java.util.ArrayList;

/**
 * Let's say field is empty if you can't make a combo,
 * field is saturated if it's not empty and there's plenty of blocks and some free space,
 * field is oversaturated if block percentage is above a certain threshold
 * and field is full if there's no free space.
 * <p>
 * The desirable period is like that:
 * 1) It's little when field is empty;
 * 2) It's manageable when field is saturated, ie you can set up huge combos in time;
 * 3) It will fill the field in the desirable time after the point of oversaturation.
 * <p>
 * Plus, it goes down a bit every time you deny a spawn.
 */
public class SpawnPeriod {
    private static final int MIN_PERIOD = 100;

    public static SpawnPeriod newFastStart(FieldStructure fieldStructure, GameSettings settings) {
        int minCombo = settings.getMinCombo();

        int saturationPoint = minCombo * 3;
        int size = fieldStructure.fieldSize;
        int oversaturationPoint = size - minCombo * 2;

        if (saturationPoint >= oversaturationPoint)
            throw new RuntimeException("minCombo is too big and fieldSize is too small");

        ArrayList<Integer> separators = new ArrayList<Integer>(4);
        separators.add(saturationPoint / 2);
        separators.add(saturationPoint);
        separators.add((oversaturationPoint + saturationPoint) / 2);
        separators.add(oversaturationPoint);

        int maxPeriod = settings.getPeriod();
        int spawnsToFill = Math.max(1, (size - oversaturationPoint) / settings.getSpawn());

        System.out.println("time to fill from oversaturation = " + maxPeriod * spawnsToFill);

        ArrayList<Integer> periods = new ArrayList<Integer>(5);
        periods.add(Math.round(0.2f * maxPeriod));
        periods.add(Math.round(0.3f * maxPeriod));
        periods.add(Math.round(0.5f * maxPeriod));
        periods.add(Math.round(0.8f * maxPeriod));
        periods.add(maxPeriod);

        PiecewiseConstantFunction<Integer, Integer> period =
                new PiecewiseConstantFunction<>(separators, periods);

        return new SpawnPeriod(period);
    }

    private PiecewiseConstantFunction<Integer, Integer> basePeriod;

    private SpawnPeriod(PiecewiseConstantFunction<Integer, Integer> period) {
        basePeriod = period;
    }

    public int getSpawnPeriod(FieldState fieldState, ConfigState configState) {
        int occupiedCells = fieldState.getNumberOfBlocks();
        int spawnsDenied = configState.getSpawnsDenied();

        int base = basePeriod.getValueAt(occupiedCells);
        int decrementFromDenies = 15 * spawnsDenied;

        return Math.max(MIN_PERIOD, base - decrementFromDenies);
    }
}
