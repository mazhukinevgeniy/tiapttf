package homemade.game.model.spawn;

import homemade.game.CellCode;
import homemade.game.Game;
import homemade.game.model.GameModelLinker;
import homemade.game.model.NumberPool;
import homemade.game.model.cellmap.CellMap;
import homemade.utils.timer.QuickTimer;
import homemade.utils.timer.TimerTaskPerformer;

import java.util.Map;

/**
 * Created by user3 on 07.04.2016.
 */
public class SpawnManager
{
    private boolean paused = false;

    private QuickTimer timer;

    private SpawnPeriod period;
    private BlockSpawner spawner;

    private GameModelLinker linker;

    public SpawnManager(GameModelLinker linker, CellMap cellMap, NumberPool numberPool)
    {
        this.linker = linker;

        spawner = new BlockSpawner(cellMap, numberPool);
        period = new SpawnPeriod();

        timer = new QuickTimer(new SpawnTimerTaskPerformer(), Game.SPAWN_PERIOD);
        //TODO: measure and show the actual period
        //TODO: manipulate period
        //TODO: call timer.stop() when game is over
    }

    public void toggleSpawnPause()
    {
        paused = !paused;
    }

    private class SpawnTimerTaskPerformer implements TimerTaskPerformer
    {
        @Override
        public void handleTimerTask()
        {
            if (!paused)
            {
                timer.setPeriod(period.getSpawnPeriod());

                Map<CellCode, Integer> changes = spawner.spawnBlocks();
                changes.putAll(spawner.markCells(Game.SIMULTANEOUS_SPAWN));
                //can do because first call doesn't interfere with the second
                //TODO: make it a single call to spawner to reduce map creation

                linker.requestSpawn(changes);
            }

        }
    }
}
