package homemade.game.model.cellmap;

import homemade.game.Game;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;

import java.util.ArrayList;

//TODO: this class is not supposed to store fieldstructure components in future
public class Cell
{
    private CellCode cellCode;

    static ArrayList<Cell> createLinkedCells(FieldStructure structure)
    {
        int size = structure.getFieldSize();
        int width = structure.getWidth();
        int height = structure.getHeight();
        
        ArrayList<Cell> cells = new ArrayList<Cell>(size);

        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++)
            {
                cells.add(new Cell(structure.getCellCode(i, j)));
            }

        Cell cell;

        //top line
        for (int i = 0; i < width; i++)
        {
            cell = cells.get(i);
            cell.neighbours[Direction.TOP.ordinal()] = null;
            cell.links[Direction.TOP.ordinal()] = null;
        }

        //left line
        for (int i = 0; i < height; i++)
        {
            cell = cells.get(width * i);
            cell.neighbours[Direction.LEFT.ordinal()] = null;
            cell.links[Direction.LEFT.ordinal()] = null;
        }

        //main cycle
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                addNeighbours(structure, cells, structure.getCellCode(i, j));
            }


        return cells;
    }

    private static void addNeighbours(FieldStructure structure, ArrayList<Cell> cells, CellCode cellCode)
    {
        int cellCodeVal = cellCode.value();
        Cell cell = cells.get(cellCodeVal);

        if (!cellCode.onBorder(Direction.RIGHT))
        {
            Cell right = cells.get(cellCodeVal + 1);
            Link link = new Link(cellCode.linkNumber(structure.getCellCode(cellCode, Direction.RIGHT)));

            cell.neighbours[Direction.RIGHT.ordinal()] = right;
            right.neighbours[Direction.LEFT.ordinal()] = cell;

            cell.links[Direction.RIGHT.ordinal()] = link;
            right.links[Direction.LEFT.ordinal()] = link;
        }
        else
        {
            cell.neighbours[Direction.RIGHT.ordinal()] = null;
            cell.links[Direction.RIGHT.ordinal()] = null;
        }

        if (!cellCode.onBorder(Direction.BOTTOM))
        {
            Cell bottom = cells.get(cellCodeVal + structure.getWidth());
            Link link = new Link(cellCode.linkNumber(structure.getCellCode(cellCode, Direction.BOTTOM)));

            cell.neighbours[Direction.BOTTOM.ordinal()] = bottom;
            bottom.neighbours[Direction.TOP.ordinal()] = cell;

            cell.links[Direction.BOTTOM.ordinal()] = link;
            bottom.links[Direction.TOP.ordinal()] = link;
        }
        else
        {
            cell.neighbours[Direction.BOTTOM.ordinal()] = null;
            cell.links[Direction.BOTTOM.ordinal()] = null;
        }
    }


    private Cell[] neighbours = new Cell[4];
    private Link[] links = new Link[4];

    private int value;

    private Cell(CellCode cellCode)
    {
        value = Game.CELL_EMPTY;
        this.cellCode = cellCode;
    }

    public Cell neighbour(Direction direction)
    {
        return neighbours[direction.ordinal()];
    }

    public Link link(Direction direction)
    {
        return links[direction.ordinal()];
    }

    public int getValue()
    {
        return value;
    }
    public CellCode getCode() { return cellCode; }

    void setValue(int newVal)
    {
        value = newVal;

        for (Direction direction : Direction.values())
        {
            if (link(direction) != null)
            {
                int outerValue = neighbour(direction).value;
                int multiplier = direction.getMultiplier();

                links[direction.ordinal()].value =
                        value > 0 &&
                        outerValue > 0 &&
                        multiplier * value > multiplier * outerValue;
            }
        }
    }
}
