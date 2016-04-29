package homemade.game.model;

import homemade.game.Cell;
import homemade.game.CellState;
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
import homemade.game.model.combo.ComboPack;
import homemade.game.model.selection.BlockSelection;
import homemade.game.model.spawn.SpawnManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameModelLinker
{
    private FieldStructure structure;
    private CellStates cellStates;

    private CellMap cellMap;
    private ComboDetector comboDetector;
    private GameScore gameScore;
    private CellStatePool cellStatePool;
    private SpawnManager spawner;
    private ArrayBasedGameState state;

    private GameController controller;

    private GameState lastGameState;
    private BlockSelection selection;
    private GameMode mode;

    GameModelLinker(FieldStructure structure, GameSettings settings, GameController controller)
    {
        initialize(structure, settings, controller);
    }

    private synchronized void initialize(FieldStructure structure, GameSettings settings, GameController controller)
    {
        this.controller = controller;
        this.structure = structure;

        cellStates = new CellStates(structure.getFieldSize());

        cellStatePool = new CellStatePool(structure.getFieldSize(), cellStates);
        cellMap = new CellMap(structure, cellStates);

        CellMapReader readOnlyMap = cellMap;

        state = new ArrayBasedGameState(structure, cellStates);
        lastGameState = state.getImmutableCopy();

        comboDetector = new ComboDetector(structure, settings, readOnlyMap, controller);
        gameScore = new GameScore(structure, controller, settings);
        spawner = new SpawnManager(this, settings, cellStatePool);

        selection = new BlockSelection(this);

        mode = settings.gameMode();
        if (mode == GameMode.TURN_BASED)
        {
            for (int i = 0; i < 2; i++)
                requestSpawn();
        }
    }

    synchronized public FieldStructure getStructure() { return structure; }
    synchronized public CellMapReader getMapReader()
    {
        return cellMap;
    }
    synchronized public CellStates getCellStates()
    {
        return cellStates;
    }

    synchronized BlockSelection getSelection()
    {
        return selection;
    }

    /**
     * Makes sense to leave it package internal because GameModel might want to force stop if user quits before losing
     * //TODO: implement said option and remove these comments
     */
    synchronized void stopAllFacilities()
    {
        spawner.spawnTimer().stop();
        controller.gameOver();
    }

    synchronized void togglePause()
    {
        spawner.spawnTimer().toggleSpawnPause();
    }

    synchronized void killRandomBlocks()
    {
        updateState(cellMap.applyCascadeChanges(spawner.spawnDeadBlocks()));
    }


    synchronized public void requestSpawn()
    {
        requestSpawn(new HashSet<>(), new ComboPack());
    }

    synchronized private void requestSpawn(Set<CellCode> updatedCells, ComboPack combos)
    {
        Map<CellCode, CellState> spawnedBlocks = spawner.spawnBlocks();
        Set<CellCode> spawn = cellMap.applyCascadeChanges(spawnedBlocks);

        updatedCells.addAll(spawn);
        ComboPack newCombos = comboDetector.findCombos(spawn);
        combos.append(newCombos);

        updatedCells.addAll(removeCombos(newCombos));

        Map<CellCode, CellState> marks = spawner.markCellsForSpawn();
        if (marks.size() == 0)
        {
            stopAllFacilities();
        }
        else
        {
            updatedCells.addAll(cellMap.applyCascadeChanges(marks));
        }

        updateState(updatedCells, combos);
    }

    /**
     * Could overload method for complicated movements
     */
    synchronized public void tryCascadeChanges(CellCode moveFromCell, CellCode moveToCell)
    {
        boolean riskOfSpawnDenial = false;

        if (cellMap.getCell(moveToCell).type() == Cell.MARKED_FOR_SPAWN)
            riskOfSpawnDenial = true;

        CellState cellFrom = cellMap.getCell(moveFromCell);
        CellState cellTo = cellMap.getCell(moveToCell);

        if (cellTo.isFreeForMove() && cellFrom.isOccupiedByBlock())
        {
            Map<CellCode, CellState> tmpMap = new HashMap<>();
            tmpMap.put(moveFromCell, cellStates.getState(riskOfSpawnDenial ? Cell.DEAD_BLOCK : Cell.EMPTY));
            tmpMap.put(moveToCell, cellFrom);

            Set<CellCode> updatedCells = cellMap.applyCascadeChanges(tmpMap);

            ComboPack combos = comboDetector.findCombos(updatedCells);

            updatedCells.addAll(removeCombos(combos));

            if (mode == GameMode.TURN_BASED && combos.numberOfCombos() == 0)
            {
                requestSpawn(updatedCells, combos);
            }
            else
            {
                updateState(updatedCells, combos);
            }

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
    synchronized public GameState lastGameState()
    {
        return lastGameState;
    }

    private Set<CellCode> removeCombos(ComboPack combos)
    {
        Map<CellCode, CellState> removedCells = new HashMap<>();

        for (CellCode cellCode : combos.cellSet())
        {
            cellStatePool.freeState(cellMap.getCell(cellCode));

            removedCells.put(cellCode, cellStates.getState(Cell.EMPTY));
        }

        cellMap.applyCascadeChanges(removedCells);

        return removedCells.keySet();
    }

    private void updateState(Set<CellCode> changedCells, ComboPack combos)
    {
        gameScore.handleCombos(combos);

        updateState(changedCells);
    }

    private void updateState(Set<CellCode> changedCells)
    {
        if (changedCells.size() > 0)
        {
            selection.updateSelectionState();

            Map<CellCode, CellState> updatedCells = new HashMap<>();
            Map<LinkCode, Direction> updatedLinks = new HashMap<>();
            Map<LinkCode, Integer> updatedChains = new HashMap<>();

            for (CellCode cellCode : changedCells)
            {
                updatedCells.put(cellCode, cellMap.getCell(cellCode));

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
