package homemade.game.model;

import homemade.game.Cell;
import homemade.game.CellState;

import java.util.LinkedList;
import java.util.Random;

public class BlockPool
{
    private LinkedList<CellState> available;
    private Random random;

    BlockPool(int max, CellStates states)
    {
        available = new LinkedList<>();

        for (int i = 0; i < max; i++)
        {
            available.add(states.getState(Cell.OCCUPIED, i + 1));
        }

        random = new Random();
    }

    public CellState takeBlock()
    {
        int length = available.size();
        int position = random.nextInt(length);

        return available.remove(position);
    }

    public void freeBlock(CellState state)
    {
        available.add(state);
    }

    public int blocksAvailable()
    {
        return available.size();
    }

}
