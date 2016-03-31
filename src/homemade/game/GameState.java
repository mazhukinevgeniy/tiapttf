package homemade.game;

/**
 * Created by user3 on 24.03.2016.
 */
public interface GameState
{
    public int getCellValue(CellCode cellCode);

    public boolean getLinkBetweenCells(int linkNumber);

    public GameState getImmutableCopy();
}
