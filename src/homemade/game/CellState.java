package homemade.game;

/**
 * Immutable
 */
public class CellState
{
    private int code;

    public CellState(int code)
    {
        this.code = code;
    }

    public int value()
    {
        return code;
    }

    public boolean isOccupied()
    {
        return code > 0;
    }

    //TODO: add info about type (empty, normal block, dead block, super block etc)
}
