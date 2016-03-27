package homemade.shell.model;

/**
 * Created by Marid on 27.03.2016.
 */
public class Settings
{
    public static Parameter<Boolean> isRealTime = new Parameter<>("isRealTime", false);
    public static Parameter<Integer> simultaneousSpawn = new Parameter<>("simultaneousSpawn", 3);
    public static Parameter<Integer> spawnPeriod = new Parameter<>("spawnPeriod", 1000);
}
