package homemade.game.model;

/**
 * Created by user3 on 22.03.2016.
 */
public class GameModel
{
    protected Field field;

    public GameModel(int width, int height)
    {
        this.field = new Field(width, height);


    }
}
