package homemade.game.model;

import homemade.game.Game;
import homemade.game.GameState;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user3 on 22.03.2016.
 */
public class GameModel
{
    private ArrayBasedGameState gameStateSnapshot;

    private Field field;
    private Timer timer;

    public GameModel()
    {
        this.field = new Field();
        this.gameStateSnapshot = new ArrayBasedGameState(this.field.cloneToArray());

        this.timer = new Timer();

        long delay = 500; //time before the timertask is executed
        long period = 1000; //TODO: move to settings
        this.timer.schedule(new GameTimerTask(this), delay, period);
        //TODO: measure and show the actual period

    }

    void handleTimerTask()
    {
        this.field.spawnBlocks();
        this.field.markCells(Game.SIMULTANEOUS_SPAWN);

        this.gameStateSnapshot = new ArrayBasedGameState(this.field.cloneToArray());
    }

    public GameState copyGameState()
    {
        return this.gameStateSnapshot;
    }

    public void gameOver()
    {
        this.timer.cancel();
        this.timer.purge();
    }

    public void blockMoveRequested(int cellCodeFrom, int cellCodeTo)
    {
        if (this.field.tryMoveBlock(cellCodeFrom, cellCodeTo))
        {
            this.gameStateSnapshot = new ArrayBasedGameState(this.field.cloneToArray());
        }
    }

    private class GameTimerTask extends TimerTask
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
}
