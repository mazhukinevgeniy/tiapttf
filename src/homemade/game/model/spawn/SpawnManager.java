package homemade.game.model.spawn;

import homemade.game.CellState;
import homemade.game.GameSettings;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.CellStatePool;
import homemade.game.model.GameModelLinker;
import homemade.game.model.cellmap.CellMapReader;

import java.util.Map;

/**
 * Created by user3 on 07.04.2016.
 */
public class SpawnManager
{
    private SpawnTimer timer;

    private BlockSpawner spawner;
    private SpawnPlanner planner;

    private int simultaneousSpawn;

    private FieldStructure structure;

    public SpawnManager(GameModelLinker linker, GameSettings settings, CellStatePool cellStatePool)
    {
        structure = linker.getStructure();

        CellMapReader cellMap = linker.getMapReader();
        spawner = new BlockSpawner(cellMap, cellStatePool);
        planner = new SpawnPlanner(cellMap, cellStatePool, linker.getCellStates());

        GameSettings.GameMode mode = settings.gameMode();
        simultaneousSpawn = settings.maxSpawn();

        if (mode == GameSettings.GameMode.TURN_BASED)
            timer = new SpawnTimer.EmptyTimer();
        else if (mode == GameSettings.GameMode.REAL_TIME)
            timer = new DynamicPeriodTimer(linker, settings);
        else
            throw new RuntimeException("unknown game mode");
    }

    public Map<CellCode, CellState> spawnBlocks()
    {
        return spawner.spawnBlocks(structure.getCellCodeIterator());
    }

    public Map<CellCode, CellState> markCells()
    {
        return planner.markCells(structure.getCellCodeIterator(), simultaneousSpawn);
    }

    public SpawnTimer spawnTimer()
    {
        return timer;
    }
}
