package homemade.game.model.spawn;

import homemade.game.GameSettings;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.loop.GameEvent;
import homemade.game.loop.GameEventHandler;
import homemade.game.loop.GameOver;
import homemade.game.loop.TimeElapsed;
import homemade.game.model.*;
import homemade.game.model.cellmap.CellMapReader;

import java.util.LinkedList;
import java.util.Map;

public class SpawnManager implements GameEventHandler<GameEvent> {
    private SpawnTimer timer;

    private BlockSpawner spawner;
    private CellMarker cellMarker;

    private int simultaneousSpawn;

    private FieldStructure structure;

    public SpawnManager(GameModelLinker linker, BlockValuePool blockValuePool) {
        structure = linker.getStructure();

        CellMapReader cellMap = linker.getMapReader();
        spawner = new BlockSpawner(cellMap, blockValuePool);
        cellMarker = new CellMarker(cellMap, blockValuePool);

        GameSettings settings = linker.getSettings();
        GameSettings.GameMode mode = settings.gameMode;
        simultaneousSpawn = settings.spawn;

        if (mode == GameSettings.GameMode.TURN_BASED)
            timer = new SpawnTimer.EmptyTimer();
        else if (mode == GameSettings.GameMode.REAL_TIME)
            timer = new DynamicPeriodTimer(linker, settings);
        else
            throw new RuntimeException("unknown game mode");
    }

    @Override
    public void handle(GameEvent event) {
        if (event instanceof GameOver) {
            timer.stop();
        } else if (event instanceof TimeElapsed) {
            timer.timeElapsed(((TimeElapsed) event).getDiffMs());
        }
    }

    public Map<CellCode, CellState> spawnBlocks() {
        int blocksToImmobilize = 1; //let's say it's either 0 or 1
        //TODO: add mechanic to determine whether we immobilize blocks or not

        return spawner.spawnBlocks(structure.getCellCodeIterator(), blocksToImmobilize);
    }

    public Map<CellCode, CellState> markCellsForSpawn() {
        return cellMarker.markForSpawn(structure.getCellCodeIterator(), simultaneousSpawn);
    }

    public Map<CellCode, CellState> markBlocksWithEffects(LinkedList<ComboEffect> effects) {
        return cellMarker.markBlocks(structure.getCellCodeIterator(), effects);
    }

    public Map<CellCode, CellState> spawnDeadBlocks() {
        return cellMarker.markAnyCell(structure.getCellCodeIterator(), Cell.DEAD_BLOCK, 5);
    }

    public Map<CellCode, CellState> removeRandomBlocks() {
        return cellMarker.markAnyCell(structure.getCellCodeIterator(), Cell.EMPTY, 25);
    }

    public SpawnTimer spawnTimer() {
        return timer;
    }
}
