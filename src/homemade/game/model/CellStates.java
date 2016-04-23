package homemade.game.model;

import homemade.game.Cell;
import homemade.game.CellState;

import java.util.*;

public class CellStates
{
    private ArrayList<CellState> occupied;
    private Map<Cell, CellState> simpleStates;

    CellStates(int maxBlockValue)
    {
        occupied = new ArrayList<>(maxBlockValue);

        for (int i = 0; i < maxBlockValue; i++)
            occupied.add(new CellState(Cell.OCCUPIED, i + 1));

        simpleStates = new EnumMap<>(Cell.class);
        Set<Cell> simpleTypes = EnumSet.of(Cell.EMPTY, Cell.MARKED_FOR_SPAWN);

        for (Cell type : simpleTypes)
            simpleStates.put(type, new CellState(type, 0));
    }

    public CellState getState(Cell type)
    {
        return getState(type, Cell.DEFAULT_VALUE);
    }

    public CellState getState(Cell type, int value)
    {
        if (value == Cell.DEFAULT_VALUE)
            return simpleStates.get(type);
        else
            return occupied.get(value - 1);
    }
}
