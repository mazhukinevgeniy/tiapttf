package homemade.game.model.cellmap;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.LinkCode;

/**
 * Use it to be sure your class has read-only access to CellMap
 */
public interface CellMapReader
{
    int getCellValue(CellCode cellCode);

    Direction getLinkDirection(CellCode cellA, CellCode cellB);
    Direction getLinkDirection(LinkCode linkCode);

    int getChainLength(LinkCode linkCode);
}