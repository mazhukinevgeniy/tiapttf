package homemade.game.model.cellstates;

import homemade.game.Cell;
import homemade.game.CellState;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class SimpleState extends CellState {
    private static Map<Cell, CellState> simpleStates = new EnumMap<>(Cell.class);

    static {
        for (Cell type : EnumSet.of(Cell.EMPTY, Cell.MARKED_FOR_SPAWN, Cell.DEAD_BLOCK)) {
            simpleStates.put(type, new SimpleState(type));
        }
    }

    public static CellState getSimpleState(Cell type) {
        return simpleStates.get(type);
    }

    private SimpleState(Cell type) {
        super(type);
    }
}
