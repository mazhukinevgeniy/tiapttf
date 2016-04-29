package homemade.game.model.combo;

import homemade.game.Combo;
import homemade.game.GameSettings;
import homemade.game.controller.BlockRemovalHandler;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.model.cellmap.CellMapReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ComboDetector
{
    private ArrayList<CellCode> tmpStorage;
    //it's probably better than reallocating every time
    //TODO: check if it is

    private FieldStructure structure;
    private CellMapReader cellMap;
    private BlockRemovalHandler blockRemovalHandler;

    private int minCombo;

    public ComboDetector(FieldStructure structure, GameSettings settings, CellMapReader cellMap, BlockRemovalHandler blockRemovalHandler)
    {
        int maxCellsPerLine = structure.getMaxDimension();
        tmpStorage = new ArrayList<CellCode>(maxCellsPerLine);

        this.structure = structure;
        this.cellMap = cellMap;
        this.blockRemovalHandler = blockRemovalHandler;

        minCombo = settings.minCombo();
    }

    public ComboPack findCombos(Set<CellCode> starts)
    {
        int numberOfStarts = starts.size();

        Set<Integer> horizontals = new HashSet<Integer>(numberOfStarts);
        Set<Integer> verticals = new HashSet<Integer>(numberOfStarts);

        for (CellCode cellCode : starts)
        {
            horizontals.add(cellCode.y());
            verticals.add(cellCode.x());
        }

        int hSize = horizontals.size();
        int vSize = verticals.size();

        ComboPack pack = new ComboPack();

        for (int horizontal : horizontals)
        {
            iterateThroughTheLine(pack, structure.getCellCode(0, horizontal), Direction.RIGHT);
        }

        for (int vertical : verticals)
        {
            iterateThroughTheLine(pack, structure.getCellCode(vertical, 0), Direction.BOTTOM);
        }

        return pack;
    }

    /**
     * @param start cellCode of the beginning
     * @param mainDirection where to look for the next cell
     */
    private void iterateThroughTheLine(ComboPack pack, CellCode start, Direction mainDirection)
    {
        CellCode currentCell = start;

        while (currentCell != null)
        {
            CellCode nextCell = currentCell.neighbour(mainDirection);
            LinkCode nextLink = null;

            if (nextCell != null)
            {
                nextLink = structure.getLinkCode(currentCell, nextCell);

                int comboLength = cellMap.getChainLength(nextLink);

                if (comboLength >= minCombo)
                {
                    tmpStorage.clear();

                    tmpStorage.add(currentCell);
                    blockRemovalHandler.blockRemoved(currentCell);
                    //TODO: overcome code duplication

                    Direction comboDirection = cellMap.getLinkDirection(nextLink);

                    while (nextCell != null && cellMap.getLinkDirection(currentCell, nextCell) == comboDirection)
                    {
                        tmpStorage.add(nextCell);
                        blockRemovalHandler.blockRemoved(nextCell);

                        currentCell = nextCell;
                        nextCell = currentCell.neighbour(mainDirection);
                    }

                    pack.add(new Combo(new HashSet<>(tmpStorage)));
                }
                else
                {
                    currentCell = nextCell;
                }
            }
            else
            {
                currentCell = null;
            }
        }

    }
}