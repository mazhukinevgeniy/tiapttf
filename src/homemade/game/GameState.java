package homemade.game;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.LinkCode;

/**
 * Created by user3 on 24.03.2016.
 */
public interface GameState
{
    public int numberOfBlocks();
    public int getSpawnsDenied();

    public CellState getCellState(CellCode cellCode);

    public Direction getLinkBetweenCells(LinkCode linkCode);
    public int getChainLength(LinkCode linkCode);

    public GameState getImmutableCopy();
}
