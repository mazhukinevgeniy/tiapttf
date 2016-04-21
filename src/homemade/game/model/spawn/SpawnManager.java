package homemade.game.model.spawn;

import homemade.game.GameSettings;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.GameModelLinker;
import homemade.game.model.NumberPool;
import homemade.game.model.cellmap.CellMapReader;
import homemade.utils.timer.QuickTimer;
import homemade.utils.timer.TimerTaskPerformer;

import java.util.Map;

/**
 * Created by user3 on 07.04.2016.
 */
public class SpawnManager
{
    static final int SIMULTANEOUS_SPAWN = 3;

    private boolean paused = false;

    private QuickTimer timer;
    private SpawnPeriod period;

    private BlockSpawner spawner;
    private SpawnPlanner planner;

    private GameModelLinker linker;
    private FieldStructure structure;

    public SpawnManager(GameModelLinker linker, GameSettings settings, CellMapReader cellMap, NumberPool numberPool)
    {
        this.linker = linker;
        structure = linker.getStructure();

        spawner = new BlockSpawner(cellMap, numberPool);
        planner = new SpawnPlanner(cellMap, numberPool);

        period = SpawnPeriod.newFastStart(linker, settings);

        timer = new QuickTimer(new SpawnTimerTaskPerformer(), period.getSpawnPeriod());
        //TODO: measure and show the actual period
    }

    public void toggleSpawnPause()
    {
        paused = !paused;
    }

    public void stop()
    {
        timer.stop();
        paused = true;
    }

    public Map<CellCode, Integer> spawnBlocks()
    {
        return spawner.spawnBlocks(structure.getCellCodeIterator());
    }

    public Map<CellCode, Integer> markCells()
    {
        return planner.markCells(structure.getCellCodeIterator(), SIMULTANEOUS_SPAWN);
    }

    private class SpawnTimerTaskPerformer implements TimerTaskPerformer
    {
        @Override
        public void handleTimerTask()
        {
            System.out.println("spawn timer task " + this.toString());

            if (!paused)
            {
                timer.setPeriod(period.getSpawnPeriod());

                linker.requestSpawn();
            }
        }
    }
}
