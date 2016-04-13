package homemade.game.model.spawn;

import homemade.game.fieldstructure.CellCode;
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
    private static final int SPAWN_PERIOD = 1000;
    static final int SIMULTANEOUS_SPAWN = 3;

    private boolean paused = false;

    private QuickTimer timer;

    private SpawnPeriod period;
    private BlockSpawner spawner;

    private GameModelLinker linker;

    public SpawnManager(GameModelLinker linker, CellMap cellMap, NumberPool numberPool)
    {
        this.linker = linker;

        spawner = new BlockSpawner(cellMap, numberPool);
        period = new SpawnPeriod(linker);

        timer = new QuickTimer(new SpawnTimerTaskPerformer(), SPAWN_PERIOD);
        //TODO: measure and show the actual period
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
                changes.putAll(spawner.markCells(SIMULTANEOUS_SPAWN));
                //can do because first call doesn't interfere with the second
                //TODO: make it a single call to spawner to reduce map creation

                linker.requestSpawn(changes);
            }

        }
    }
}
