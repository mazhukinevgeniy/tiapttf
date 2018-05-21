package homemade.game.model.cellstates;

import homemade.game.Cell;
import homemade.game.CellState;
import homemade.game.ComboEffect;

public class BlockState extends CellState {
    private final boolean movable;
    private final int cellValue;
    private final ComboEffect effect;

    public BlockState(int value, boolean movable, ComboEffect effect) {
        super(Cell.OCCUPIED);

        this.cellValue = value;
        this.movable = movable;
        this.effect = effect;
    }

    @Override
    public int value() {
        return cellValue;
    }

    @Override
    public ComboEffect effect() {
        return effect;
    }

    @Override
    public boolean isMovableBlock() {
        return movable;
    }
}
