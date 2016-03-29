package homemade.game.model;

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
        int fieldSize = Game.FIELD_WIDTH * Game.FIELD_HEIGHT;

        field = new int[fieldSize];

        for (int i = 0; i < fieldSize; i++)
        {
            field[i] = Game.CELL_EMPTY;
        }

        links = new boolean[fieldSize * 2]; //based on Game.linkNumber

        for (int i = 0; i < fieldSize * 2; i++)
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


    synchronized void updateFieldSnapshot(Map<Integer, Integer> updates, CellMap map)
    {
        immutableCopy = null;

        int[] shifts = {-1, 1, Game.FIELD_WIDTH, -Game.FIELD_WIDTH};

        //for each cell update links as well

        Set<Integer> keys = updates.keySet();

        for (int key : keys)
        {
            int value = updates.get(key);

            field[key] = value;

            for (int i = 0; i < 4; i++)
            {
                int adjCellCode = key + shifts[i];

                if (!(adjCellCode < 0)) //filters out left and top imaginary links
                {
                    int linkNumber = Game.linkNumber(key, adjCellCode);
                    Link link = map.links.get(linkNumber);

                    if (link != null) //filters out right and bottom imaginary links
                    {
                        links[linkNumber] = link.value;
                    }
                }
            }

        }
    }


    @Override
    public int getCellValue(int cellX, int cellY)
    {
        //System.out.println("returning cell value from gamestate, " + this.field[cellX + Game.FIELD_WIDTH * cellY]);
        return this.field[cellX + Game.FIELD_WIDTH * cellY];
    }

    @Override
    public boolean getLinkBetweenCells(int cellCodeA, int cellCodeB)
    {
        return false;
    }

    @Override
    synchronized public GameState getImmutableCopy()
    {
        if (immutableCopy == null)
            immutableCopy = new ArrayBasedGameState(field, links);

        return immutableCopy;
    }
}
