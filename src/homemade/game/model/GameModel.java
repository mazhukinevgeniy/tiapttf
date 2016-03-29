package homemade.game.model;

import homemade.game.Game;
import homemade.game.GameState;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user3 on 22.03.2016.
 */
public class GameModel
{
    static Map<Integer, Integer> getCleanIntIntMap()
    {
        return new HashMap<Integer, Integer>();
    }

    private GameState gameState;

    private CellMap cellMap;
    private BlockSpawner spawner;
    private Timer timer;

    public GameModel()
    {
        NumberPool numberPool = new NumberPool(Game.FIELD_WIDTH * Game.FIELD_HEIGHT);

        ArrayBasedGameState gameStateTracker = new ArrayBasedGameState();
        cellMap = new CellMap(gameStateTracker, numberPool);
        gameState = gameStateTracker;

        spawner = new BlockSpawner(cellMap, numberPool);

        this.timer = new Timer();

        long delay = 0;
        long period = Game.SPAWN_PERIOD;
        this.timer.schedule(new GameTimerTask(this), delay, period);
        //TODO: measure and show the actual period

    }

    void handleTimerTask()
    {
        Map<Integer, Integer> changes = spawner.spawnBlocks();
        changes.putAll(spawner.markCells(Game.SIMULTANEOUS_SPAWN));
        //can do because first call doesn't interfere with the second

        cellMap.applyCascadeChanges(changes);
    }

    public GameState copyGameState()
    {
        return gameState.getImmutableCopy();
    }

    public void gameOver()
    {
        this.timer.cancel();
        this.timer.purge();
    }

    public void blockMoveRequested(int cellCodeFrom, int cellCodeTo)
    {
        cellMap.tryCascadeChanges(cellCodeFrom, cellCodeTo);
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
