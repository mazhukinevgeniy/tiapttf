package homemade.game;

import homemade.game.fieldstructure.CellCode;

/**
 * Created by user3 on 25.03.2016.
 */
public interface SelectionState
{
    public boolean isSelected(CellCode cellCode);
    public boolean canMoveTo(CellCode cellCode);
}
