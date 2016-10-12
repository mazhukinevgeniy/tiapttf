package homemade.menu.model.settings;

import java.util.HashMap;
import java.util.Map;

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
    private void setCommonValues (Map<String, Object> values)
    {
        values.put(Settings.Name.animatedLinks, false);
    }

    //TURN BASED EASY
    private void initializeTurnBasedEasyMode(Map<String, Object> values)
    {
        values.put(Settings.Name.simultaneousSpawn, 3);
        values.put(Settings.Name.spawnPeriod, 2000);
        values.put(Settings.Name.comboLength, 3);
    }

    //TURN BASED MEDIUM
    private void initializeTurnBasedMediumMode(Map<String, Object> values)
    {
        values.put(Settings.Name.simultaneousSpawn, 4);
        values.put(Settings.Name.spawnPeriod, 2000);
        values.put(Settings.Name.comboLength, 5);
    }

    //TURN BASED HARD
    private void initializeTurnBasedHardMode(Map<String, Object> values)
    {
        values.put(Settings.Name.simultaneousSpawn, 5);
        values.put(Settings.Name.spawnPeriod, 1500);
        values.put(Settings.Name.comboLength, 6);
    }

    //REALTIME EASY
    private void initializeRealtimeEasyMode(Map<String, Object> values)
    {
        values.put(Settings.Name.simultaneousSpawn, 3);
        values.put(Settings.Name.spawnPeriod, 2000);
        values.put(Settings.Name.comboLength, 3);
    }

    //REALTIME MEDIUM
    private void initializeRealtimeMediumMode(Map<String, Object> values)
    {
        values.put(Settings.Name.simultaneousSpawn, 4);
        values.put(Settings.Name.spawnPeriod, 2000);
        values.put(Settings.Name.comboLength, 5);
    }

    //REALTIME HARD
    private void initializeRealtimeHardMode(Map<String, Object> values)
    {
        values.put(Settings.Name.simultaneousSpawn, 5);
        values.put(Settings.Name.spawnPeriod, 1500);
        values.put(Settings.Name.comboLength, 6);
    }

    public Map<String, Object> getValues(GroupCode group, ModeCode mode)
    {
        Map<String, Object> values = new HashMap<>();

        setCommonValues(values);

        if (group == GroupCode.TURN_BASED)
        {
            values.put(Settings.Name.isRealTime, false);
            if (mode == ModeCode.EASY)
            {
                initializeTurnBasedEasyMode(values);
            }
            else if (mode == ModeCode.MEDIUM)
            {
                initializeTurnBasedMediumMode(values);
            }
            else if (mode == ModeCode.HARD)
            {
                initializeTurnBasedHardMode(values);
            }
        }
        else if (group == GroupCode.REALTIME)
        {
            Object put = values.put(Settings.Name.isRealTime, true);
            if (mode == ModeCode.EASY)
            {
                initializeRealtimeEasyMode(values);
            }
            else if (mode == ModeCode.MEDIUM)
            {
                initializeRealtimeMediumMode(values);
            }
            else if (mode == ModeCode.HARD)
            {
                initializeRealtimeHardMode(values);
            }
        }

        return values;
    }
}
