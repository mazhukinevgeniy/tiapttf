package homemade.game;

import homemade.game.fieldstructure.CellCode;

public interface SelectionState {
    boolean isSelected(CellCode cellCode);

    boolean canMoveTo(CellCode cellCode);
}
