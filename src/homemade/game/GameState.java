package homemade.game;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.model.CellState;

public interface GameState {
    int numberOfMovableBlocks();

    int numberOfBlocks();

    int spawnsDenied();

    int gameScore();

    int globalMultiplier();

    CellState getCellState(CellCode cellCode);

    Direction getLinkBetweenCells(LinkCode linkCode);

    int getChainLength(LinkCode linkCode);
}
