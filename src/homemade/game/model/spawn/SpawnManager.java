package homemade.game.model.spawn;

import homemade.game.Cell;
import homemade.game.CellState;
import homemade.game.GameSettings;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.BlockPool;
import homemade.game.model.GameModelLinker;
import homemade.game.model.cellmap.CellMapReader;

import java.util.Map;

public class SpawnManager
{
    private SpawnTimer timer;

    private BlockSpawner spawner;
    private CellMarker cellMarker;

    private int simultaneousSpawn;

    private FieldStructure structure;

    public SpawnManager(GameModelLinker linker, BlockPool blockPool)
    {
        structure = linker.getStructure();

        CellMapReader cellMap = linker.getMapReader();
        spawner = new BlockSpawner(cellMap, blockPool);
        cellMarker = new CellMarker(cellMap, blockPool, linker.getCellStates());

        GameSettings settings = linker.getSettings();
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

    public Map<CellCode, CellState> markCellsForSpawn()
    {
        return cellMarker.markForSpawn(structure.getCellCodeIterator(), simultaneousSpawn);
    }

    public Map<CellCode, CellState> spawnDeadBlocks()
    {
        return cellMarker.markAnyCell(structure.getCellCodeIterator(), Cell.DEAD_BLOCK, 5);
    }

    public SpawnTimer spawnTimer()
    {
        return timer;
    }
}
