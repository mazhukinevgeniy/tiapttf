package homemade.game;

import java.util.Comparator;

public enum ComboEffect {
    //care: all prices must be different
    EXTRA_MULTIPLIER(1), IMMOVABLE(-1), JUST_EXTRA_TIER(2), EXPLOSION(4);

    private int price;

    private ComboEffect(int price) {
        this.price = price;
    }

    public int tierBonus() {
        return this == EXTRA_MULTIPLIER ? 0 : 1;
    }

    public int multiplierBonus() {
        return this == EXTRA_MULTIPLIER ? 1 : 0;
    }

    public int getPrice() {
        return price;
    }

    /**
     * Default sort with returned comparator will sort
     * from the most expensive to the least expensive effects.
     * <p>
     * null isn't supported
     */
    public static Comparator<ComboEffect> getComparator() {
        return new ComboEffectComparator();
    }

    private static class ComboEffectComparator implements Comparator<ComboEffect> {
        public int compare(ComboEffect o1, ComboEffect o2) {
            if (o1 == null || o2 == null)
                throw new NullPointerException();

            return o2.getPrice() - o1.getPrice();
        }
    }
}