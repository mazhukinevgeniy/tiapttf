package homemade.game.model.combo;

import homemade.game.Combo;
import homemade.game.fieldstructure.CellCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a set of combos, created "simultaneously".
 *
 * Must be synchronized externally!
 */
public class ComboPack
{
    private ArrayList<Combo> combos;

    private int tier;

    public ComboPack()
    {
        combos = new ArrayList<>();
        tier = 0;
    }

    void add(Combo combo)
    {
        combos.add(combo);
        tier += combo.getTier();
    }

    public void append(ComboPack anotherPack)
    {
        combos.addAll(anotherPack.combos);
        tier += anotherPack.tier;
    }

    public Set<CellCode> cellSet()
    {
        Set<CellCode> cells = new HashSet<>();

        for (Combo combo : combos)
            cells.addAll(combo.toSet());

        return cells;
    }

    public Iterator<Combo> comboIterator()
    {
        return combos.iterator();
    }

    public int numberOfCombos()
    {
        return combos.size();
    }

    public int packTier()
    {
        return tier;
    }
}
