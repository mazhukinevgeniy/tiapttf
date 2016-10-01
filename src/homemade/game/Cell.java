package homemade.game;

public enum Cell
{
    EMPTY, MARKED_FOR_SPAWN, OCCUPIED, DEAD_BLOCK;

    public enum ComboEffect
    {
        EXPLOSION, EXTRA_COMBO_TIER, IMMOVABLE
    }

    public static final int DEFAULT_VALUE = 0;
}