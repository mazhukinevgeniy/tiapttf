package homemade.game.model;

import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;

import java.util.Map;
import java.util.Set;

/**
 * Created by user3 on 24.03.2016.
 */
class ArrayBasedGameState implements GameState
{
    private int[] field;
    private Direction[] links;
    private GameState immutableCopy;

    private int cellsOccupied;
    private int spawnsDenied;

    ArrayBasedGameState(FieldStructure structure)
    {
        int fieldSize = structure.getFieldSize();

        field = new int[fieldSize];

        for (int i = 0; i < fieldSize; i++)
        {
            field[i] = Game.CELL_EMPTY;
        }

        int numberOfLinks = structure.getNumberOfLinks();

        links = new Direction[numberOfLinks];

        for (int i = 0; i < numberOfLinks; i++)
        {
            links[i] = null;
        }

        cellsOccupied = 0;
        spawnsDenied = 0;
    }

    private ArrayBasedGameState(int[] fieldData, Direction[] linkData, int spawnsDenied)
    {
        field = fieldData;
        links = linkData;

        if (fieldData == null || linkData == null)
            throw new Error("corrupted game state copy has been created");
        else
        {
            int counter = 0;

            for (int value : fieldData)
            {
                if (value > 0)
                    counter++;
            }

            cellsOccupied = counter;
        }

        this.spawnsDenied = spawnsDenied;
    }

    synchronized void incrementDenyCounter()
    {
        spawnsDenied++;
    }


    synchronized void updateFieldSnapshot(Map<CellCode, Integer> cellUpdates, Map<LinkCode, Direction> linkUpdates)
    {
        immutableCopy = null;


        Set<CellCode> keys = cellUpdates.keySet();

        for (CellCode key : keys)
        {
            int value = cellUpdates.get(key);

            field[key.intCode()] = value;
        }

        Set<LinkCode> linkKeys = linkUpdates.keySet();

        for (LinkCode key : linkKeys)
        {
            links[key.intCode()] = linkUpdates.get(key);
        }
    }

    @Override
    public int getSpawnsDenied()
    {
        return spawnsDenied;
    }

    @Override
    public int getCellsOccupied()
    {
        return cellsOccupied;
    }

    @Override
    public int getCellValue(CellCode cellCode)
    {
        return this.field[cellCode.intCode()];
    }

    @Override
    public Direction getLinkBetweenCells(LinkCode linkCode)
    {
        return links[linkCode.intCode()];
    }

    @Override
    synchronized public GameState getImmutableCopy()
    {
        if (immutableCopy == null)
            immutableCopy = new ArrayBasedGameState(field, links, spawnsDenied);

        return immutableCopy;
    }
}
