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

    private final static int SIMULATTANEOUS_SPAWN = 0;
    private final static int SPAWN_PERIOD = 1;
    private final static int COMBO_LENGTH = 2;

    private EnumMap<Mode, EnumMap<Difficulty, List<Integer>>> presetsMap;

    public Presets() {
        presetsMap = new EnumMap<>(Mode.class);
        presetsMap.put(Mode.TURN_BASED, new EnumMap<>(Difficulty.class));
        presetsMap.put(Mode.REALTIME, new EnumMap<>(Difficulty.class));

        //...Arrays.asList(simultaneousSpawn, spawnPeriod, comboLength); //see consts above
        presetsMap.get(Mode.TURN_BASED).put(Difficulty.EASY, Arrays.asList(2, 6000, 5));
        presetsMap.get(Mode.TURN_BASED).put(Difficulty.MEDIUM, Arrays.asList(3, 6000, 5));
        presetsMap.get(Mode.TURN_BASED).put(Difficulty.HARD, Arrays.asList(4, 6000, 5));

        presetsMap.get(Mode.REALTIME).put(Difficulty.EASY, Arrays.asList(20, 200, 3));
        presetsMap.get(Mode.REALTIME).put(Difficulty.MEDIUM, Arrays.asList(3, 6000, 5));
        presetsMap.get(Mode.REALTIME).put(Difficulty.HARD, Arrays.asList(3, 3500, 5));

    }

    public List<Parameter<?>> getPresets(Mode mode, Difficulty difficulty) {
        List<Parameter<?>> parameters = new ArrayList<>();

        boolean isRealTime = false;
        if (mode == Mode.REALTIME) {
            isRealTime = true;
        }
        parameters.add(new Parameter<>(Settings.Name.isRealTime, isRealTime));

        String[] patametersName = new String[]{Settings.Name.simultaneousSpawn,
                Settings.Name.spawnPeriod,
                Settings.Name.comboLength};
        List<Integer> values = presetsMap.get(mode).get(difficulty);

        for (int index : new int[]{SIMULATTANEOUS_SPAWN, SPAWN_PERIOD, COMBO_LENGTH}) {
            parameters.add(new Parameter<Object>(patametersName[index], values.get(index)));
        }

        return parameters;
    }
}