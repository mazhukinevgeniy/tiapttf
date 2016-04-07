package homemade.game.model;

import homemade.game.CellCode;
import homemade.game.Game;
import homemade.game.GameState;

import java.util.Map;
import java.util.Set;

/**
 * Created by user3 on 24.03.2016.
 */
class ArrayBasedGameState implements GameState
{
    private int[] field;
    private boolean[] links;
    private GameState immutableCopy;

    ArrayBasedGameState()
    {
        field = new int[Game.FIELD_SIZE];

        for (int i = 0; i < Game.FIELD_SIZE; i++)
        {
            field[i] = Game.CELL_EMPTY;
        }

        links = new boolean[Game.FIELD_SIZE * 2];

        for (int i = 0; i < Game.FIELD_SIZE * 2; i++)
        {
            links[i] = false;
        }
    }

    ArrayBasedGameState(int[] fieldData, boolean[] linkData)
    {
        field = fieldData;
        links = linkData;

        if (fieldData == null || linkData == null)
            throw new Error("corrupted game state copy has been created");
    }


    synchronized void updateFieldSnapshot(Map<CellCode, Integer> cellUpdates, Map<Integer, Boolean> linkUpdates)
    {
        immutableCopy = null;


        Set<CellCode> keys = cellUpdates.keySet();

        for (CellCode key : keys)
        {
            int value = cellUpdates.get(key);

            field[key.value()] = value;
        }

        Set<Integer> linkKeys = linkUpdates.keySet();

        for (int key : linkKeys)
        {
            links[key] = linkUpdates.get(key);
        }
    }


    @Override
    public int getCellValue(CellCode cellCode)
    {
        return this.field[cellCode.value()];
    }

    @Override
    public boolean getLinkBetweenCells(int linkNumber)
    {
        return links[linkNumber];
    }

    @Override
    synchronized public GameState getImmutableCopy()
    {
        if (immutableCopy == null)
            immutableCopy = new ArrayBasedGameState(field, links);

        return immutableCopy;
    }
}
