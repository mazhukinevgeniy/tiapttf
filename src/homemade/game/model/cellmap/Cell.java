package homemade.game.model.cellmap;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.Game;

import java.util.ArrayList;

/**
 * Created by user3 on 27.03.2016.
 */
public class Cell
{
    private int code;

    static ArrayList<Cell> createLinkedCells()
    {
        ArrayList<Cell> cells = new ArrayList<Cell>(Game.FIELD_SIZE);

        for (int i = 0; i < Game.FIELD_SIZE; i++)
        {
            cells.add(new Cell(i));
        }

        Cell cell;

        //top line
        for (int i = 0; i < Game.FIELD_WIDTH; i++)
        {
            cell = cells.get(i);
            cell.neighbours[Direction.TOP.ordinal()] = null;
            cell.links[Direction.TOP.ordinal()] = null;
        }

        //left line
        for (int i = 0; i < Game.FIELD_HEIGHT; i++)
        {
            cell = cells.get(Game.FIELD_WIDTH * i);
            cell.neighbours[Direction.LEFT.ordinal()] = null;
            cell.links[Direction.LEFT.ordinal()] = null;
        }

        //main cycle
        for (int i = 0; i < Game.FIELD_WIDTH; i++)
            for (int j = 0; j < Game.FIELD_HEIGHT; j++)
            {
                addNeighbours(cells, CellCode.getFor(i, j));
            }


        return cells;
    }

    private static void addNeighbours(ArrayList<Cell> cells, CellCode cellCode)
    {
        int cellCodeVal = cellCode.value();
        Cell cell = cells.get(cellCodeVal);

        if (!cellCode.onBorder(Direction.RIGHT))
        {
            Cell right = cells.get(cellCodeVal + 1);
            Link link = new Link(cellCode.linkNumber(Direction.RIGHT));

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
            Cell bottom = cells.get(cellCodeVal + Game.FIELD_WIDTH);
            Link link = new Link(cellCode.linkNumber(Direction.BOTTOM));

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

    private Cell(int code)
    {
        value = Game.CELL_EMPTY;
        this.code = code;
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
    public CellCode getCode() { return CellCode.getFor(code); }

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
