package homemade.game.model;

import homemade.game.Game;
import homemade.game.GameState;

import java.util.ArrayList;
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
        int[] shifts = new int[4];
        shifts[Cell.BOTTOM] = Game.FIELD_WIDTH;
        shifts[Cell.TOP] = -Game.FIELD_WIDTH;
        shifts[Cell.LEFT] = -1;
        shifts[Cell.RIGHT] = 1;

        //for each cell update links as well

        Set<Integer> keys = updates.keySet();

        for (int key : keys)
        {
            int value = updates.get(key);

            field[key] = value;


            directions.clear();

            int keyX = key % Game.FIELD_WIDTH;
            int keyY = key / Game.FIELD_WIDTH;

            if (keyX > 0) directions.add(Cell.LEFT);
            if (keyX < Game.FIELD_WIDTH - 1) directions.add(Cell.RIGHT);
            if (keyY > 0) directions.add(Cell.TOP);
            if (keyY < Game.FIELD_HEIGHT - 1) directions.add(Cell.BOTTOM);
            //TODO: rewrite if the performance will require it

            for (int direction : directions) //checking links for every direction applicable
            {
                int linkNumber = Game.linkNumber(key, key + shifts[direction]);

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
