package homemade.game;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.LinkCode;

/**
 * Created by user3 on 24.03.2016.
 */
public interface GameState
{
    public int getCellsOccupied();
    public int getSpawnsDenied();

    public int getCellValue(CellCode cellCode);

    public Direction getLinkBetweenCells(LinkCode linkCode);
    public int getChainLength(LinkCode linkCode);

    public GameState getImmutableCopy();
}
