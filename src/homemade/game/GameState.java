package homemade.game;

import homemade.game.fieldstructure.CellCode;

/**
 * Created by user3 on 24.03.2016.
 */
public interface GameState
{
    public int getCellsOccupied();
    public int getSpawnsDenied();

    public int getCellValue(CellCode cellCode);

    public boolean getLinkBetweenCells(int linkNumber);

    public GameState getImmutableCopy();
}
