package homemade.game.model;

import homemade.game.Game;

import java.util.ArrayList;

/**
 * Created by user3 on 27.03.2016.
 */
class Cell
{
    static final int LEFT = 0;
    static final int RIGHT = 1;
    static final int TOP = 2;
    static final int BOTTOM = 3;

    static ArrayList<Cell> createLinkedCells()
    {
        ArrayList<Cell> cells = new ArrayList<Cell>(Game.FIELD_WIDTH * Game.FIELD_HEIGHT);

        for (int i = 0; i < Game.FIELD_WIDTH * Game.FIELD_HEIGHT; i++)
        {
            cells.add(new Cell());
        }

        Cell cell;

        //top line
        for (int i = 0; i < Game.FIELD_WIDTH; i++)
        {
            cell = cells.get(i);
            cell.neighbours[Cell.TOP] = null;
            cell.links[Cell.TOP] = null;
        }

        //left line
        for (int i = 0; i < Game.FIELD_HEIGHT; i++)
        {
            cell = cells.get(Game.FIELD_WIDTH * i);
            cell.neighbours[Cell.LEFT] = null;
            cell.links[Cell.LEFT] = null;
        }

        //bottom line
        for (int i = 0; i < Game.FIELD_WIDTH - 1; i++)
        {
            addNeighbours(cells, (Game.FIELD_HEIGHT - 1) * Game.FIELD_WIDTH + i, true, false);
        }

        //right line
        for (int i = 0; i < Game.FIELD_HEIGHT - 1; i++)
        {
            addNeighbours(cells, (Game.FIELD_HEIGHT - 1) * Game.FIELD_WIDTH + i, false, true);
        }

        //bottom right cell
        addNeighbours(cells, Game.FIELD_HEIGHT * Game.FIELD_WIDTH - 1, false, false);


        //main cycle
        for (int i = 0; i < Game.FIELD_WIDTH - 1; i++)
            for (int j = 0; j < Game.FIELD_HEIGHT - 1; j++)
            {
                addNeighbours(cells, i + j * Game.FIELD_WIDTH, true, true);
            }


        return cells;
    }

    private static void addNeighbours(ArrayList<Cell> cells, int cellCode, boolean addRight, boolean addBottom)
    {
        Cell cell = cells.get(cellCode);

        if (addRight)
        {
            Cell right = cells.get(cellCode + 1);
            Link link = new Link();

            cell.neighbours[Cell.RIGHT] = right;
            right.neighbours[Cell.LEFT] = cell;

            cell.links[Cell.RIGHT] = link;
            right.links[Cell.LEFT] = link;
        }
        else
        {
            cell.neighbours[Cell.RIGHT] = null;
            cell.links[Cell.RIGHT] = null;
        }

        if (addBottom)
        {
            Cell bottom = cells.get(cellCode + Game.FIELD_WIDTH);
            Link link = new Link();

            cell.neighbours[Cell.BOTTOM] = bottom;
            bottom.neighbours[Cell.TOP] = cell;

            cell.links[Cell.BOTTOM] = link;
            bottom.links[Cell.TOP] = link;
        }
        else
        {
            cell.neighbours[Cell.BOTTOM] = null;
            cell.links[Cell.BOTTOM] = null;
        }
    }


    private Cell[] neighbours = new Cell[4];
    private Link[] links = new Link[4];

    private int value;

    private Cell()
    {
        value = Game.CELL_EMPTY;
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

    void setValue(int newVal)
    {
        //TODO: make all the fun things

        value = newVal;
    }
}
