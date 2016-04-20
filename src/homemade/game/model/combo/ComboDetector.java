package homemade.game.model.combo;

import homemade.game.GameSettings;
import homemade.game.controller.BlockRemovalHandler;
import homemade.game.controller.GameController;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.model.cellmap.CellMapReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user3 on 26.03.2016.
 */
public class ComboDetector
{

    public static ComboDetector initializeComboDetection(FieldStructure structure, GameSettings settings, CellMapReader cellMap, GameController controller)
    {
        GameScore gameScore = new GameScore(structure, controller, settings);

        return new ComboDetector(structure, settings, cellMap, controller, gameScore);
    }


    private ArrayList<CellCode> tmpStorage;
    //it's probably better than reallocating every time

    private FieldStructure structure;
    private CellMapReader cellMap;
    private GameScore gameScore;
    private BlockRemovalHandler blockRemovalHandler;

    private int minCombo;

    private ComboDetector(FieldStructure structure, GameSettings settings, CellMapReader cellMap, BlockRemovalHandler blockRemovalHandler, GameScore gameScore)
    {
        int maxCellsPerLine = structure.getMaxDimension();
        tmpStorage = new ArrayList<CellCode>(maxCellsPerLine);

        this.structure = structure;
        this.cellMap = cellMap;
        this.gameScore = gameScore;
        this.blockRemovalHandler = blockRemovalHandler;

        minCombo = settings.minCombo();
    }

    /**
     * @return Set of cellCodes in which blocks are bound to be removed because they were part of a combo
     */
    public Set<CellCode> findCellsToRemove(Set<CellCode> starts)
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

        int maxCellsToRemove = hSize * structure.getWidth() + vSize * structure.getHeight() - hSize * vSize;

        HashSet<CellCode> cellsToRemove = new HashSet<CellCode>(maxCellsToRemove);

        for (int horizontal : horizontals)
        {
            iterateThroughTheLine(cellsToRemove, structure.getCellCode(0, horizontal), Direction.RIGHT);
        }

        for (int vertical : verticals)
        {
            iterateThroughTheLine(cellsToRemove, structure.getCellCode(vertical, 0), Direction.BOTTOM);
        }

        return cellsToRemove;
    }

    /**
     *
     * @param set storage for found cells
     * @param start cellCode of the beginning
     * @param mainDirection where to look for the next cell
     */
    private void iterateThroughTheLine(Set<CellCode> set, CellCode start, Direction mainDirection)
    {
        tmpStorage.clear();

        //System.out.println("start = " + start + ", direction = " + direction);

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
                    gameScore.handleCombo(comboLength);

                    String report = "";

                    tmpStorage.add(currentCell);
                    blockRemovalHandler.blockRemoved(currentCell);
                    report = " " + cellMap.getCellValue(currentCell) + report;
                    //TODO: overcome code duplication

                    Direction comboDirection = cellMap.getLinkDirection(nextLink);

                    while (nextCell != null && cellMap.getLinkDirection(currentCell, nextCell) == comboDirection)
                    {
                        tmpStorage.add(nextCell);
                        blockRemovalHandler.blockRemoved(nextCell);
                        report = " " + cellMap.getCellValue(nextCell) + report;

                        currentCell = nextCell;
                        nextCell = currentCell.neighbour(mainDirection);
                    }

                    System.out.println("combo is" + report);
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

        set.addAll(tmpStorage);
    }
}