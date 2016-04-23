package homemade.game.model;

import homemade.game.CellState;
import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;

import java.util.Map;

/**
 * Must be synchronized externally!
 */
class ArrayBasedGameState implements GameState
{
    private CellState[] field;
    private Direction[] links;
    private int[] chainLengths;

    private GameState immutableCopy;

    private int cellsOccupied;
    private int spawnsDenied;

    ArrayBasedGameState(FieldStructure structure)
    {
        int fieldSize = structure.getFieldSize();

        field = new CellState[fieldSize];

        for (int i = 0; i < fieldSize; i++)
        {
            field[i] = new CellState(Game.CELL_EMPTY);
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

    private ArrayBasedGameState(CellState[] fieldData, Direction[] linkData, int[] chainData, int spawnsDenied)
    {
        if (fieldData == null || linkData == null || chainData == null)
            throw new Error("corrupted game state copy has been created");

        field = fieldData.clone();
        links = linkData.clone();
        chainLengths = chainData.clone();


        int counter = 0;

        for (CellState cell : fieldData)
        {
            if (cell.isOccupied())
                counter++;
        }

        cellsOccupied = counter;

        this.spawnsDenied = spawnsDenied;
    }

    void incrementDenyCounter()
    {
        spawnsDenied++;
    }


    void updateFieldSnapshot( Map<CellCode, CellState> cellUpdates,
                              Map<LinkCode, Direction> linkUpdates,
                              Map<LinkCode, Integer> chainUpdates)
    {
        immutableCopy = null;


        for (Map.Entry<CellCode, CellState> entry : cellUpdates.entrySet())
        {
            int pos = entry.getKey().hashCode();
            CellState newState = entry.getValue();
            CellState oldState = field[pos];

            if (!oldState.isOccupied() && newState.isOccupied())
                cellsOccupied++;
            else if (oldState.isOccupied() && !newState.isOccupied())
                cellsOccupied--;

            field[pos] = newState;
        }

        for (Map.Entry<LinkCode, Direction> entry : linkUpdates.entrySet())
        {
            links[entry.getKey().hashCode()] = entry.getValue();
        }

        for (Map.Entry<LinkCode, Integer> entry : chainUpdates.entrySet())
        {
            chainLengths[entry.getKey().hashCode()] = entry.getValue();
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
    public CellState getCellState(CellCode cellCode)
    {
        return this.field[cellCode.hashCode()];
    }

    @Override
    public Direction getLinkBetweenCells(LinkCode linkCode)
    {
        return links[linkCode.hashCode()];
    }

    @Override
    public int getChainLength(LinkCode linkCode)
    {
        return chainLengths[linkCode.hashCode()];
    }

    @Override
    public GameState getImmutableCopy()
    {
        if (immutableCopy == null)
            immutableCopy = new ArrayBasedGameState(field, links, chainLengths, spawnsDenied);

        return immutableCopy;
    }
}
