package homemade.game.model.cellmap;

import homemade.game.Game;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;

import java.util.ArrayList;

//TODO: this class is not supposed to store fieldstructure components in future
public class Cell
{

    static ArrayList<Cell> createLinkedCells(FieldStructure structure)
    {
        int size = structure.getFieldSize();
        int width = structure.getWidth();
        int height = structure.getHeight();
        
        ArrayList<Cell> cells = new ArrayList<Cell>(size);

        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++)
            {
                cells.add(new Cell());
            }

        Cell cell;

        //top line
        for (int i = 0; i < width; i++)
        {
            cell = cells.get(i);
            cell.links[Direction.TOP.ordinal()] = null;
        }

        //left line
        for (int i = 0; i < height; i++)
        {
            cell = cells.get(width * i);
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
            Link link = new Link(cellCode.linkNumber(cellCode.neighbour(Direction.RIGHT)));

            cell.links[Direction.RIGHT.ordinal()] = link;
            right.links[Direction.LEFT.ordinal()] = link;
        }
        else
        {
            cell.links[Direction.RIGHT.ordinal()] = null;
        }

        if (!cellCode.onBorder(Direction.BOTTOM))
        {
            Cell bottom = cells.get(cellCodeVal + structure.getWidth());
            Link link = new Link(cellCode.linkNumber(cellCode.neighbour(Direction.BOTTOM)));

            cell.links[Direction.BOTTOM.ordinal()] = link;
            bottom.links[Direction.TOP.ordinal()] = link;
        }
        else
        {
            cell.links[Direction.BOTTOM.ordinal()] = null;
        }
    }

    int value;
    Link[] links = new Link[4];

    private Cell()
    {
        value = Game.CELL_EMPTY;
    }

    public Link link(Direction direction)
    {
        return links[direction.ordinal()];
    }//TODO: don't store it there

    public int getValue()
    {
        return value;
    }

}
