package homemade.game;

/**
 * Created by user3 on 24.03.2016.
 */
public interface GameState
{
    public int getCellValue(int cellX, int cellY);

    public boolean getLinkBetweenCells(int cellCodeA, int cellCodeB);

    public GameState getImmutableCopy();
}
