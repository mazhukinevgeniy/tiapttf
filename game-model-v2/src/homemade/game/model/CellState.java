package homemade.game.model;

import java.util.EnumSet;
import java.util.Set;

/**
 * Extensions of this class must be immutable
 */
public abstract class CellState {
    private static Set<Cell> cellFreeToMove = EnumSet.of(Cell.EMPTY, Cell.MARKED_FOR_SPAWN);
    private static Set<Cell> blocks = EnumSet.of(Cell.OCCUPIED, Cell.DEAD_BLOCK);

    public final static int UNDEFINED_VALUE = -1;
    public final static ComboEffect UNDEFINED_COMBO_EFFECT = null;
    public final static boolean UNDEFINED_PROPERTY = false;

    private final Cell cellType;

    protected CellState(Cell cellType) {
        this.cellType = cellType;
    }

    public int value() {
        return UNDEFINED_VALUE;
    }

    public ComboEffect effect() {
        return UNDEFINED_COMBO_EFFECT;
    }

    public boolean isMovableBlock() {
        return UNDEFINED_PROPERTY;
    }

    /**
     * I think we need this method for view
     */
    public final Cell type() {
        return cellType;
    }

    public final boolean isAliveBlock() { //TODO: find a better method name
        return cellType == Cell.OCCUPIED;
    }

    public final boolean isAnyBlock() {
        return blocks.contains(cellType);
    }

    public final boolean isFreeForMove() {
        return cellFreeToMove.contains(cellType);
    }

    public final boolean isFreeForSpawn() {
        return cellType == Cell.EMPTY;
    }
}