package homemade.game.model.spawn;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;
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
    static final int SIMULTANEOUS_SPAWN = 3;

    private boolean paused = false;

    private QuickTimer timer;

    private SpawnPeriod period;
    private BlockSpawner spawner;

    private GameModelLinker linker;

    public SpawnManager(GameModelLinker linker, CellMap cellMap, NumberPool numberPool)
    {
        this.linker = linker;

        spawner = new BlockSpawner(this, cellMap, numberPool);
        period = new SpawnPeriod(linker);

        timer = new QuickTimer(new SpawnTimerTaskPerformer(), period.getSpawnPeriod());
        //TODO: measure and show the actual period
    }

    public void toggleSpawnPause()
    {
        paused = !paused;
    }

    void spawnImpossible()
    {
        timer.stop();
        paused = true;

        linker.stopAllFacilities();
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

                FieldStructure structure = linker.getStructure();

                Map<CellCode, Integer> changes = spawner.spawnBlocks(structure.getCellCodeIterator());
                changes.putAll(spawner.markCells(structure.getCellCodeIterator(), SIMULTANEOUS_SPAWN));

                linker.requestSpawn(changes);
            }

        }
    }
}
