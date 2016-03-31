package homemade.game.model;

import homemade.game.CellCode;
import homemade.game.Direction;
import homemade.game.Game;

import java.util.ArrayList;

/**
 * Created by user3 on 27.03.2016.
 */
class Cell
{
    private int code;

    private static final int [] directionMultiplier = {1, -1, 1, -1};
    //if direction to the other cell is 0 or 2: linked = thisvalue * 1 > 1 * othervalue
    //if direction is 1 or 3: linked = thisvalue * - 1 > -1 * othervalue
    //TODO: generate based on named constants

    static ArrayList<Cell> createLinkedCells()
    {
        ArrayList<Cell> cells = new ArrayList<Cell>(Game.FIELD_WIDTH * Game.FIELD_HEIGHT);

        for (int i = 0; i < Game.FIELD_WIDTH * Game.FIELD_HEIGHT; i++)
        {
            cells.add(new Cell(i));
        }

        Cell cell;

        //top line
        for (int i = 0; i < Game.FIELD_WIDTH; i++)
        {
            cell = cells.get(i);
            cell.neighbours[Direction.TOP] = null;
            cell.links[Direction.TOP] = null;
        }

        //left line
        for (int i = 0; i < Game.FIELD_HEIGHT; i++)
        {
            cell = cells.get(Game.FIELD_WIDTH * i);
            cell.neighbours[Direction.LEFT] = null;
            cell.links[Direction.LEFT] = null;
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
            Link link = new Link();

            cell.neighbours[Direction.RIGHT] = right;
            right.neighbours[Direction.LEFT] = cell;

            cell.links[Direction.RIGHT] = link;
            right.links[Direction.LEFT] = link;
        }
        else
        {
            cell.neighbours[Direction.RIGHT] = null;
            cell.links[Direction.RIGHT] = null;
        }

        if (!cellCode.onBorder(Direction.BOTTOM))
        {
            Cell bottom = cells.get(cellCodeVal + Game.FIELD_WIDTH);
            Link link = new Link();

            cell.neighbours[Direction.BOTTOM] = bottom;
            bottom.neighbours[Direction.TOP] = cell;

            cell.links[Direction.BOTTOM] = link;
            bottom.links[Direction.TOP] = link;
        }
        else
        {
            cell.neighbours[Direction.BOTTOM] = null;
            cell.links[Direction.BOTTOM] = null;
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

    Cell neighbour(int directionCode)
    {
        return neighbours[directionCode];
    }

    Link link(int directionCode)
    {
        return links[directionCode];
    }

    int getValue()
    {
        return value;
    }
    int getCode() { return code; }

    void setValue(int newVal)
    {
        value = newVal;

        for (int i = 0; i < 4; i++) //works if direction codes are 0 1 2 3
        {
            if (links[i] != null)
            {
                int outerValue = neighbours[i].value;
                int dirMult = directionMultiplier[i];

                links[i].value =
                        value > 0 &&
                        outerValue > 0 &&
                        dirMult * value > dirMult * outerValue;
            }
        }
    }
}
