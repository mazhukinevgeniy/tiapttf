package homemade.game;

import homemade.game.Cell.ComboEffect;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Immutable
 */
public class CellState
{
    private static Map<Cell, CellState> simpleStates = new EnumMap<>(Cell.class);

    static
    {
        for (Cell type : EnumSet.of(Cell.EMPTY, Cell.MARKED_FOR_SPAWN, Cell.DEAD_BLOCK))
        {
            simpleStates.put(type, new CellState(type, Cell.DEFAULT_VALUE, null));
        }
    }

    public static CellState simpleState(Cell type)
    {
        return simpleStates.get(type);
    }


    private static Set<Cell> cellFreeToMove = EnumSet.of(Cell.EMPTY, Cell.MARKED_FOR_SPAWN);
    private static Set<Cell> blocks = EnumSet.of(Cell.OCCUPIED, Cell.DEAD_BLOCK);


    private Cell cellType;
    private int cellValue;
    private ComboEffect effect;

    public CellState(int value)
    {
        this(Cell.OCCUPIED, value, null);

        if (value == Cell.DEFAULT_VALUE)
            throw new RuntimeException("incorrect creation of cellState");
    }

    public CellState(CellState baseState, ComboEffect effect)
    {
        this(Cell.OCCUPIED, baseState.cellValue, effect);

        if (!baseState.isNormalBlock())
            throw new RuntimeException("incorrect creation of cellState with effect");
    }

    public CellState(Cell type, int code, ComboEffect effect)
    {
        cellType = type;
        cellValue = code;
        this.effect = effect;
    }

    public int value()
    {
        return cellValue;
    }

    public ComboEffect effect()
    {
        return effect;
    }

    /**
     * I think we need this method for view
     */
    public Cell type()
    {
        return cellType;
    }

    public boolean isNormalBlock()
    {
        return cellType == Cell.OCCUPIED;
    }

    public boolean isMovable() { return isNormalBlock() && effect != ComboEffect.EXTRA_BASE_TIER; }

    public boolean isAnyBlock()
    {
        return blocks.contains(cellType);
    }


    public boolean isFreeForMove()
    {
        return cellFreeToMove.contains(cellType);
    }

    public boolean isFreeForSpawn()
    {
        return cellType == Cell.EMPTY;
    }
}