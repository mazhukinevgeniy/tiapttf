package homemade.game.model;

import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;

import java.util.Map;

/**
 * Created by user3 on 24.03.2016.
 */
class ArrayBasedGameState implements GameState
{
    private int[] field;
    private Direction[] links;
    private int[] chainLengths;

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

        chainLengths = new int[numberOfLinks];

        for (int i = 0; i < numberOfLinks; i++)
        {
            chainLengths[i] = 0;
        }

        cellsOccupied = 0;
        spawnsDenied = 0;
    }

    private ArrayBasedGameState(int[] fieldData, Direction[] linkData, int[] chainData, int spawnsDenied)
    {
        if (fieldData == null || linkData == null || chainData == null)
            throw new Error("corrupted game state copy has been created");

        field = fieldData.clone();
        links = linkData.clone();
        chainLengths = chainData.clone();


        int counter = 0;

        for (int value : fieldData)
        {
            if (value > 0)
                counter++;
        }

        cellsOccupied = counter;

        this.spawnsDenied = spawnsDenied;
    }

    synchronized void incrementDenyCounter()
    {
        spawnsDenied++;
    }


    synchronized void updateFieldSnapshot(Map<CellCode, Integer> cellUpdates,
                                          Map<LinkCode, Direction> linkUpdates,
                                          Map<LinkCode, Integer> chainUpdates)
    {
        immutableCopy = null;


        for (Map.Entry<CellCode, Integer> entry : cellUpdates.entrySet())
        {
            int pos = entry.getKey().intCode();
            int newValue = entry.getValue();
            int oldValue = field[pos];

            if (oldValue < 1 && newValue > 0)
                cellsOccupied++;
            else if (oldValue > 0 && newValue < 1)
                cellsOccupied--;

            field[pos] = newValue;
        }

        for (Map.Entry<LinkCode, Direction> entry : linkUpdates.entrySet())
        {
            links[entry.getKey().intCode()] = entry.getValue();
        }

        for (Map.Entry<LinkCode, Integer> entry : chainUpdates.entrySet())
        {
            chainLengths[entry.getKey().intCode()] = entry.getValue();
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
    public int getChainLength(LinkCode linkCode)
    {
        return chainLengths[linkCode.intCode()];
    }

    @Override
    synchronized public GameState getImmutableCopy()
    {
        if (immutableCopy == null)
            immutableCopy = new ArrayBasedGameState(field, links, chainLengths, spawnsDenied);

        return immutableCopy;
    }
}
