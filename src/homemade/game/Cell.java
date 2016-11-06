package homemade.game;

public enum Cell
{
    EMPTY, MARKED_FOR_SPAWN, OCCUPIED, DEAD_BLOCK;

    public enum ComboEffect
    {
        EXTRA_MULTIPLIER, IMMOVABLE, JUST_EXTRA_TIER, EXPLOSION;

        public int tierBonus()
        {
            return this == EXTRA_MULTIPLIER ? 0 : 1;
        }

        public int multiplierBonus()
        {
            return this == EXTRA_MULTIPLIER ? 1 : 0;
        }
    }

    public static final int DEFAULT_VALUE = 0;
}