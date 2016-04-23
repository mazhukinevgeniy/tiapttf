package homemade.game.model;

import homemade.game.Cell;
import homemade.game.CellState;

import java.util.LinkedList;
import java.util.Random;

public class CellStatePool
{
    private LinkedList<CellState> available;
    private Random random;

    CellStatePool(int max, CellStates states)
    {
        available = new LinkedList<>();

        for (int i = 0; i < max; i++)
        {
            available.add(states.getState(Cell.OCCUPIED, i + 1));
        }

        random = new Random();
    }

    public CellState takeState()
    {
        int length = available.size();
        int position = random.nextInt(length);

        return available.remove(position);
    }

    public void freeState(CellState state)
    {
        available.add(state);
    }

    public int statesAvailable()
    {
        return available.size();
    }

}
