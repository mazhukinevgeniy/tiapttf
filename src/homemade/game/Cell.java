package homemade.game;

public enum Cell
{
    EMPTY, MARKED_FOR_SPAWN, OCCUPIED, DEAD_BLOCK;

    public enum ComboEffect
    {
        IMMOVABLE, JUST_EXTRA_TIER, EXPLOSION
    }

    public static final int DEFAULT_VALUE = 0;
}