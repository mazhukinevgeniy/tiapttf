package homemade.game.model;

import homemade.game.Game;

import java.util.Timer;

/**
 * Created by user3 on 22.03.2016.
 */
public class GameModel
{
    private Field field;
    private Timer timer;

    public GameModel()
    {
        int width = Game.FIELD_WIDTH, height = Game.FIELD_HEIGHT;

        this.field = new Field(width, height);

        this.timer = new Timer();

        long delay = 500; //time before the timertask is executed
        long period = 1000; //TODO: move to settings
        this.timer.schedule(new GameTimerTask(this), delay, period);


    }

    void handleTimerTask()
    {
        int attemptsToSpawn = 3; //TODO: move to settings

        this.field.spawnBlocks();
        this.field.markCells(attemptsToSpawn);
    }

    public int [] getData()
    {
        return this.field.toArray();
    }

    public void gameOver()
    {
        this.timer.cancel();
        this.timer.purge();
    }
}
