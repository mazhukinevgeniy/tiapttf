package homemade.game.model;

import homemade.game.CellState;

import java.util.LinkedList;
import java.util.Random;

public class BlockPool
{
    private LinkedList<CellState> available;
    private Random random;

    BlockPool(int max)
    {
        available = new LinkedList<>();

        for (int i = 0; i < max; i++)
        {
            available.add(new CellState(i + 1));
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
        available.add(new CellState(state.value()));
    }

    public int blocksAvailable()
    {
        return available.size();
    }

}
