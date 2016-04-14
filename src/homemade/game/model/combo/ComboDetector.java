package homemade.game.model.combo;

import homemade.game.Game;
import homemade.game.controller.BlockRemovalHandler;
import homemade.game.controller.GameController;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.cellmap.Cell;
import homemade.game.model.cellmap.CellMap;
import homemade.game.model.cellmap.Link;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user3 on 26.03.2016.
 */
public class ComboDetector
{

    public static ComboDetector initializeComboDetection(FieldStructure structure, CellMap cellMap, GameController controller)
    {
        GameScore gameScore = new GameScore(structure, controller);

        return new ComboDetector(structure, cellMap, controller, gameScore);
    }


    private ArrayList<CellCode> tmpStorage;
    //it's probably better than reallocating every time

    private FieldStructure structure;
    private CellMap cellMap;
    private GameScore gameScore;
    private BlockRemovalHandler blockRemovalHandler;

    private ComboDetector(FieldStructure structure, CellMap cellMap, BlockRemovalHandler blockRemovalHandler, GameScore gameScore)
    {
        int maxCellsPerLine = structure.getMaxDimension();
        tmpStorage = new ArrayList<CellCode>(maxCellsPerLine);

        this.structure = structure;
        this.cellMap = cellMap;
        this.gameScore = gameScore;
        this.blockRemovalHandler = blockRemovalHandler;
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
     * @param direction where to look for the next cell
     */
    private void iterateThroughTheLine(Set<CellCode> set, CellCode start, Direction direction)
    {
        tmpStorage.clear();

        //System.out.println("start = " + start + ", direction = " + direction);

        Cell currentCell = cellMap.getCell(start);
        Cell comboStartedAt = currentCell;

        int comboLength = 1;

        while (currentCell != null)
        {
            Link link = currentCell.link(direction);
            Cell tmpNext = currentCell.neighbour(direction);

            if (link != null && link.getValue())
            {
                comboLength++;
            }
            else
            {

                if (comboLength >= Game.MIN_COMBO)
                {
                    gameScore.handleCombo(comboLength);

                    String report = "";
                    Cell tmpCell = comboStartedAt;

                    while (tmpCell != tmpNext)
                    {
                        CellCode cellCode = tmpCell.getCode();

                        report = " " + cellCode.value() + report;

                        blockRemovalHandler.blockRemoved(cellCode);
                        tmpStorage.add(cellCode);
                        tmpCell = tmpCell.neighbour(direction);
                    }

                    System.out.println("in terms of cell numbers combo is" + report);
                }

                comboStartedAt = tmpNext;
                comboLength = 1;
            }

            currentCell = tmpNext;
        }

        set.addAll(tmpStorage);
    }
}