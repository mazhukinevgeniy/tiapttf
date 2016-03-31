package homemade.game.model;

import homemade.game.CellCode;
import homemade.game.Direction;
import homemade.game.Game;
import homemade.game.GameState;

import java.util.ArrayList;
import java.util.Iterator;
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

        ArrayList<Integer> directions = new ArrayList<Integer>(4);


        Set<Integer> keys = updates.keySet();

        for (int key : keys)
        {
            int value = updates.get(key);

            field[key] = value;


            //for each cell update links as well

            directions.clear();

            CellCode cellCode = CellCode.getFor(key);
            Iterator<Integer> iterator = Direction.getIterator();

            while (iterator.hasNext())
            {
                int direction = iterator.next();

                if (!cellCode.onBorder(direction))
                    directions.add(direction);
            }

            for (int direction : directions) //checking links for every direction applicable
            {
                int linkNumber = Game.linkNumber(key, key + CellCode.getShift(direction));

                Link link = map.links.get(linkNumber);

                links[linkNumber] = link.value;
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
        return links[Game.linkNumber(cellCodeA, cellCodeB)];
    }

    @Override
    synchronized public GameState getImmutableCopy()
    {
        if (immutableCopy == null)
            immutableCopy = new ArrayBasedGameState(field, links);

        return immutableCopy;
    }
}
