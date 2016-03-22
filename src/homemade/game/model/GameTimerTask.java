package homemade.game.model;

import java.util.TimerTask;

/**
 * Created by user3 on 22.03.2016.
 */
class GameTimerTask extends TimerTask
{
    private GameModel model;

    GameTimerTask(GameModel model)
    {
        this.model = model;
    }

    @Override
    public void run()
    {
        this.model.handleTimerTask();
        //I guess it's ok to have no logic here because we might want to replace timer
    }
}
