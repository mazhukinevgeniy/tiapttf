package homemade.menu.model.settings;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class Presets
{
    public enum Mode
    {
        TURN_BASED,
        REALTIME,
        CUSTOM
    }

    public enum Difficulty
    {
        EASY,
        MEDIUM,
        HARD,
        CUSTOM
    }

    private EnumMap<Mode, EnumMap<Difficulty, List<Parameter<?>>>> presetsLists;

    public Presets()
    {
        presetsLists = new EnumMap<>(Mode.class);
        presetsLists.put(Mode.TURN_BASED, getTurnBasedMap());
        presetsLists.put(Mode.REALTIME, getRealtimeMap());
    }

    private EnumMap<Difficulty, List<Parameter<?>>> getTurnBasedMap()
    {
        EnumMap<Difficulty, List<Parameter<?>>> turnBasedMap = new EnumMap<>(Difficulty.class);

        turnBasedMap.put(Difficulty.EASY, getTurnBasedEasy());
        turnBasedMap.put(Difficulty.MEDIUM, getTurnBasedMedium());
        turnBasedMap.put(Difficulty.HARD, getTurnBasedHard());

        return turnBasedMap;
    }

    //TURN BASED EASY
    private List<Parameter<?>> getTurnBasedEasy()
    {
        List<Parameter<?>> parameters = getCommonParameters();

        parameters.add(new Parameter<>(Settings.Name.isRealTime, false));
        parameters.add(new Parameter<>(Settings.Name.simultaneousSpawn, 2));
        parameters.add(new Parameter<>(Settings.Name.spawnPeriod, 6000));
        parameters.add(new Parameter<>(Settings.Name.comboLength, 5));

        return parameters;
    }

    //TURN BASED MEDIUM
    private List<Parameter<?>> getTurnBasedMedium()
    {
        List<Parameter<?>> parameters = getCommonParameters();

        parameters.add(new Parameter<>(Settings.Name.isRealTime, false));
        parameters.add(new Parameter<>(Settings.Name.simultaneousSpawn, 3));
        parameters.add(new Parameter<>(Settings.Name.spawnPeriod, 6000));
        parameters.add(new Parameter<>(Settings.Name.comboLength, 5));

        return parameters;
    }

    //TURN BASED HARD
    private List<Parameter<?>> getTurnBasedHard()
    {
        List<Parameter<?>> parameters = getCommonParameters();

        parameters.add(new Parameter<>(Settings.Name.isRealTime, false));
        parameters.add(new Parameter<>(Settings.Name.simultaneousSpawn, 4));
        parameters.add(new Parameter<>(Settings.Name.spawnPeriod, 6000));
        parameters.add(new Parameter<>(Settings.Name.comboLength, 5));

        return parameters;
    }

    private EnumMap<Difficulty, List<Parameter<?>>> getRealtimeMap()
    {
        EnumMap<Difficulty, List<Parameter<?>>> realtimeMap = new EnumMap<>(Difficulty.class);

        realtimeMap.put(Difficulty.EASY, getRealtimeEasy());
        realtimeMap.put(Difficulty.MEDIUM, getRealtimeMedium());
        realtimeMap.put(Difficulty.HARD, getRealtimeHard());

        return realtimeMap;
    }

    //REALTIME EASY
    private List<Parameter<?>> getRealtimeEasy()
    {
        List<Parameter<?>> parameters = getCommonParameters();

        parameters.add(new Parameter<>(Settings.Name.isRealTime, true));
        parameters.add(new Parameter<>(Settings.Name.simultaneousSpawn, 20));
        parameters.add(new Parameter<>(Settings.Name.spawnPeriod, 200));
        parameters.add(new Parameter<>(Settings.Name.comboLength, 3));

        return parameters;
    }

    //REALTIME MEDIUM
    private List<Parameter<?>> getRealtimeMedium()
    {
        List<Parameter<?>> parameters = getCommonParameters();

        parameters.add(new Parameter<>(Settings.Name.isRealTime, true));
        parameters.add(new Parameter<>(Settings.Name.simultaneousSpawn, 3));
        parameters.add(new Parameter<>(Settings.Name.spawnPeriod, 6000));
        parameters.add(new Parameter<>(Settings.Name.comboLength, 5));

        return parameters;
    }

    //REALTIME HARD
    private List<Parameter<?>> getRealtimeHard()
    {
        List<Parameter<?>> parameters = getCommonParameters();

        parameters.add(new Parameter<>(Settings.Name.isRealTime, true));
        parameters.add(new Parameter<>(Settings.Name.simultaneousSpawn, 3));
        parameters.add(new Parameter<>(Settings.Name.spawnPeriod, 3500));
        parameters.add(new Parameter<>(Settings.Name.comboLength, 5));

        return parameters;
    }

    //COMMON VALUES
    private List<Parameter<?>> getCommonParameters()
    {
        List<Parameter<?>> parameters = new ArrayList<>();

        parameters.add(new Parameter<>(Settings.Name.animatedLinks, false));

        return parameters;
    }

    public List<Parameter<?>> getPresets(Mode mode, Difficulty difficulty)
    {
        return presetsLists.get(mode).get(difficulty);
    }

    //TODO: just create lists
    //store them in EnumMap<Mode, EnumMap<Difficulty, List<Parameter<?>>>
    //in getPreset(..) return new ArrayList<>(lists.get(mode).get(difficulty))
    //more readable initialization, 0 elseifs, 0 extra methods
    //also nothing has to be static
    //I'd also rename the whole class to Presets or something
    //I'm so tempted to do it myself <_<
}