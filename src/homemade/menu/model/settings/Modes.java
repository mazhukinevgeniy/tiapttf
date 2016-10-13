package homemade.menu.model.settings;

import java.util.ArrayList;
import java.util.List;

public class Modes
{
    public enum GroupCode
    {
        TURN_BASED,
        REALTIME,
        CUSTOM
    }

    public enum ModeCode
    {
        EASY,
        MEDIUM,
        HARD,
        CUSTOM
    }

    //COMMON VALUES
    private static void setCommonValues (List<Parameter<?>> parameters)
    {
        parameters.add(new Parameter<>(Boolean.class, Settings.Name.animatedLinks, false));
    }

    //TURN BASED EASY
    private static void initializeTurnBasedEasyMode(List<Parameter<?>> parameters)
    {
        parameters.add(new Parameter<>(Integer.class, Settings.Name.simultaneousSpawn, 3));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.spawnPeriod, 2000));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.comboLength, 3));
    }

    //TURN BASED MEDIUM
    private static void initializeTurnBasedMediumMode(List<Parameter<?>> parameters)
    {
        parameters.add(new Parameter<>(Integer.class, Settings.Name.simultaneousSpawn, 4));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.spawnPeriod, 2000));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.comboLength, 5));
    }

    //TURN BASED HARD
    private static void initializeTurnBasedHardMode(List<Parameter<?>> parameters)
    {
        parameters.add(new Parameter<>(Integer.class, Settings.Name.simultaneousSpawn, 5));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.spawnPeriod, 1500));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.comboLength, 6));
    }

    //REALTIME EASY
    private static void initializeRealtimeEasyMode(List<Parameter<?>> parameters)
    {
        parameters.add(new Parameter<>(Integer.class, Settings.Name.simultaneousSpawn, 3));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.spawnPeriod, 2000));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.comboLength, 3));
    }

    //REALTIME MEDIUM
    private static void initializeRealtimeMediumMode(List<Parameter<?>> parameters)
    {
        parameters.add(new Parameter<>(Integer.class, Settings.Name.simultaneousSpawn, 4));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.spawnPeriod, 2000));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.comboLength, 5));
    }

    //REALTIME HARD
    private static void initializeRealtimeHardMode(List<Parameter<?>> parameters)
    {
        parameters.add(new Parameter<>(Integer.class, Settings.Name.simultaneousSpawn, 5));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.spawnPeriod, 1500));
        parameters.add(new Parameter<>(Integer.class, Settings.Name.comboLength, 6));
    }

    public static List<Parameter<?>> getValues(GroupCode group, ModeCode mode)
    {
        //Map<String, Object> values = new HashMap<>();
        List<Parameter<?>> parameters = new ArrayList<>();;

        setCommonValues(parameters);

        if (group == GroupCode.TURN_BASED)
        {
            parameters.add(new Parameter<>(Boolean.class, Settings.Name.isRealTime, false));
            if (mode == ModeCode.EASY)
            {
                initializeTurnBasedEasyMode(parameters);
            }
            else if (mode == ModeCode.MEDIUM)
            {
                initializeTurnBasedMediumMode(parameters);
            }
            else if (mode == ModeCode.HARD)
            {
                initializeTurnBasedHardMode(parameters);
            }
        }
        else if (group == GroupCode.REALTIME)
        {
            parameters.add(new Parameter<>(Boolean.class, Settings.Name.isRealTime, true));
            if (mode == ModeCode.EASY)
            {
                initializeRealtimeEasyMode(parameters);
            }
            else if (mode == ModeCode.MEDIUM)
            {
                initializeRealtimeMediumMode(parameters);
            }
            else if (mode == ModeCode.HARD)
            {
                initializeRealtimeHardMode(parameters);
            }
        }

        return parameters;
    }
}
