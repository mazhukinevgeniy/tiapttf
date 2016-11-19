package homemade.menu.model.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

public class Presets {
    public enum Mode {
        TURN_BASED,
        REALTIME
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }
    //1) add new index for preset setting
    private final static int SIMULATTANEOUS_SPAWN = 0;
    private final static int SPAWN_PERIOD = 1;
    private final static int COMBO_LENGTH = 2;
    private final static int MAX_BLOCK_VALUE = 3;

    //2) add new index to array indexes
    private int[] indexes = new int[] {SIMULATTANEOUS_SPAWN, SPAWN_PERIOD, COMBO_LENGTH, MAX_BLOCK_VALUE};

    //3) add name new settings (order is important)
    private String[] patametersName = new String[]{Settings.Name.simultaneousSpawn,
            Settings.Name.spawnPeriod,
            Settings.Name.comboLength,
            Settings.Name.maxBlockValue};

    private EnumMap<Mode, EnumMap<Difficulty, List<Integer>>> presetsMap;

    public Presets() {
        presetsMap = new EnumMap<>(Mode.class);
        presetsMap.put(Mode.TURN_BASED, new EnumMap<>(Difficulty.class));
        presetsMap.put(Mode.REALTIME, new EnumMap<>(Difficulty.class));

    //4) add values to congruent maps
        //...Arrays.asList(simultaneousSpawn, spawnPeriod, comboLength, maxBlockValue); //see consts above
        presetsMap.get(Mode.TURN_BASED).put(Difficulty.EASY, Arrays.asList(2, 6000, 5, 81));
        presetsMap.get(Mode.TURN_BASED).put(Difficulty.MEDIUM, Arrays.asList(3, 6000, 5, 27));
        presetsMap.get(Mode.TURN_BASED).put(Difficulty.HARD, Arrays.asList(4, 6000, 5, 9));

        presetsMap.get(Mode.REALTIME).put(Difficulty.EASY, Arrays.asList(20, 200, 3, 81));
        presetsMap.get(Mode.REALTIME).put(Difficulty.MEDIUM, Arrays.asList(3, 6000, 5, 27));
        presetsMap.get(Mode.REALTIME).put(Difficulty.HARD, Arrays.asList(3, 3500, 5, 9));
    }

    public List<Parameter<?>> getPresets(Mode mode, Difficulty difficulty) {
        List<Parameter<?>> parameters = new ArrayList<>();

        boolean isRealTime = false;
        if (mode == Mode.REALTIME) {
            isRealTime = true;
        }
        parameters.add(new Parameter<>(Settings.Name.isRealTime, isRealTime));

        List<Integer> values = presetsMap.get(mode).get(difficulty);

        for (int index : indexes) {
            parameters.add(new Parameter<Object>(patametersName[index], values.get(index)));
        }

        return parameters;
    }
}