package homemade.game.model.combo;

import homemade.game.Combo;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a set of combos, created "simultaneously".
 *
 * Must be synchronized externally!
 */
public class ComboPack
{
    private ArrayList<Combo> combos;

    public ComboPack()
    {
        combos = new ArrayList<>();
    }

    public void append(ComboPack anotherPack)
    {
        combos.addAll(anotherPack.combos);
    }

    void add(Combo combo)
    {
        combos.add(combo);
    }

    int numberOfCombos()
    {
        return combos.size();
    }

    Iterator<Combo> comboIterator()
    {
        return combos.iterator();
    }

}
