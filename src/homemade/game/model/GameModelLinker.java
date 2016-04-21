package homemade.game.model;

import homemade.game.Game;
import homemade.game.GameSettings;
import homemade.game.GameSettings.GameMode;
import homemade.game.GameState;
import homemade.game.controller.GameController;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.model.cellmap.CellMap;
import homemade.game.model.cellmap.CellMapReader;
import homemade.game.model.combo.ComboDetector;
import homemade.game.model.spawn.SpawnManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameModelLinker
{
    private static final boolean AUTOCOMPLETION = true;

    private FieldStructure structure;

    private CellMap cellMap;
    private ComboDetector comboDetector;
    private NumberPool numberPool;
    private SpawnManager spawner;
    private ArrayBasedGameState state;

    private GameController controller;

    private GameState lastGameState;
    private GameMode mode;

    GameModelLinker(FieldStructure structure, GameSettings settings, GameController controller)
    {
        this.controller = controller;
        this.structure = structure;

        numberPool = new NumberPool(structure.getFieldSize());
        cellMap = new CellMap(structure);

        CellMapReader readOnlyMap = cellMap;

        state = new ArrayBasedGameState(structure);
        lastGameState = state.getImmutableCopy();

        comboDetector = ComboDetector.initializeComboDetection(structure, settings, readOnlyMap, controller);
        spawner = new SpawnManager(this, settings, readOnlyMap, numberPool);

        mode = settings.gameMode();
        if (mode == GameMode.TURN_BASED)
        {
            for (int i = 0; i < 2; i++)
                requestSpawn();
        }
    }

    public FieldStructure getStructure() { return structure; }

    /**
     * Makes sense to leave it package internal because GameModel might want to force stop if user quits before losing
     * //TODO: implement said option and remove these comments
     */
    void stopAllFacilities()
    {
        spawner.spawnTimer().stop();
        controller.gameOver();
    }

    void togglePause()
    {
        spawner.spawnTimer().toggleSpawnPause();
    }


    synchronized public void requestSpawn()
    {
        Map<CellCode, Integer> spawnedBlocks = spawner.spawnBlocks();
        actOnChangedCells(cellMap.applyCascadeChanges(spawnedBlocks));

        Map<CellCode, Integer> marks = spawner.markCells();
        if (marks.size() == 0)
        {
            stopAllFacilities();
        }
        else
        {
            actOnChangedCells(cellMap.applyCascadeChanges(marks));
        }
    }

    /**
     * Use this for handing user input: requested moves might be based on an outdated gameState
     * In this case, we should deny the attempt
     *
     * Could overload method for complicated movements
     */
    synchronized void tryCascadeChanges(CellCode moveFromCell, CellCode moveToCell)
    {
        boolean riskOfSpawnDenial = false;

        if (cellMap.getCellValue(moveToCell) == Game.CELL_MARKED_FOR_SPAWN)
            riskOfSpawnDenial = true;

        int cellFromValue = cellMap.getCellValue(moveFromCell);
        int cellToValue = cellMap.getCellValue(moveToCell);

        if (cellToValue <= 0 && cellFromValue > 0)
        {
            Map<CellCode, Integer> tmpMap = new HashMap<>();
            tmpMap.put(moveFromCell, Game.CELL_EMPTY);
            tmpMap.put(moveToCell, cellFromValue);

            actOnChangedCells(cellMap.applyCascadeChanges(tmpMap));

            if (mode == GameMode.TURN_BASED)
                requestSpawn();
            if (riskOfSpawnDenial)
                state.incrementDenyCounter();
        }
    }

    synchronized GameState copyGameState()
    {
        return lastGameState = state.getImmutableCopy();
    }

    /**
     * Use this if
     * A) you want an immutable gamestate
     * B) you don't care if it's not updated since the last external use (e.g. rendering)
     */
    public GameState lastGameState()
    {
        return lastGameState;
    }

    private void actOnChangedCells(Set<CellCode> changedCells)
    {
        if (changedCells.size() > 0)
        {
            if (AUTOCOMPLETION)
            {
                Map<CellCode, Integer> removedCells = new HashMap<>();

                Set<CellCode> cellsToRemove = comboDetector.findCellsToRemove(changedCells);

                for (CellCode cellCode : cellsToRemove)
                {
                    numberPool.freeNumber(cellMap.getCellValue(cellCode));

                    removedCells.put(cellCode, Game.CELL_EMPTY);
                }

                cellMap.applyCascadeChanges(removedCells);

                changedCells.addAll(removedCells.keySet());
            }

            updateState(changedCells); //TODO: separate it from autocompletion
        }
    }

    private void updateState(Set<CellCode> changedCells)
    {
        if (changedCells.size() > 0)
        {
            Map<CellCode, Integer> updatedCells = new HashMap<>();
            Map<LinkCode, Direction> updatedLinks = new HashMap<>();
            Map<LinkCode, Integer> updatedChains = new HashMap<>();

            for (CellCode cellCode : changedCells)
            {
                updatedCells.put(cellCode, cellMap.getCellValue(cellCode));

                for (Direction direction : Direction.values())
                {
                    CellCode neighbour = cellCode.neighbour(direction);

                    if (neighbour != null)
                    {
                        LinkCode link = structure.getLinkCode(cellCode, neighbour);
                        updatedLinks.put(link, cellMap.getLinkDirection(link));
                    }

                    CellCode previousCell = cellCode;
                    while (neighbour != null)
                    {
                        LinkCode linkCode = structure.getLinkCode(previousCell, neighbour);

                        updatedChains.put(linkCode, cellMap.getChainLength(linkCode));

                        previousCell = neighbour;
                        neighbour = previousCell.neighbour(direction);
                    }
                }
            }

            state.updateFieldSnapshot(updatedCells, updatedLinks, updatedChains);
        }
    }
}
