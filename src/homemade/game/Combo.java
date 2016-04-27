package homemade.game;

import homemade.game.fieldstructure.CellCode;

import java.util.HashSet;
import java.util.Set;

public class Combo
{
    private Set<CellCode> cells;
    private int length;

    /**
     * @param cells used without copying, creator must not modify it
     */
    public Combo(Set<CellCode> cells)
    {
        this.cells = cells;
        length = cells.size();
    }

    public int getLength()
    {
        return length;
    }

    public Set<CellCode> toSet()
    {
        return new HashSet<>(cells);
    }
}
