package homemade.game;

public enum Cell
{
    EMPTY, MARKED_FOR_SPAWN, OCCUPIED, DEAD_BLOCK;

    public enum ComboEffect
    {
        EXPLOSION, EXTRA_COMBO_TIER, EXTRA_BASE_TIER
    }

    public static final int DEFAULT_VALUE = 0;
}