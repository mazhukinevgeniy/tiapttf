package homemade.game;

import homemade.game.fieldstructure.CellCode;

import java.util.HashSet;
import java.util.Set;

public class Combo {
    private Set<CellCode> cells;
    private int tier;

    /**
     * @param cells used without copying, creator must not modify it
     */
    public Combo(Set<CellCode> cells, int tier) {
        assert tier > 0;

        this.cells = cells;
        this.tier = tier;
    }

    /**
     * @return 1 or more
     */
    public int getTier() {
        return tier;
    }

    public Set<CellCode> toSet() {
        return new HashSet<>(cells);
    }
}