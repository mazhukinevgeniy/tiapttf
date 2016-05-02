package homemade.game.model;

import homemade.game.Cell;
import homemade.game.CellState;
import homemade.game.GameSettings;
import homemade.game.GameSettings.GameMode;
import homemade.game.GameState;
import homemade.game.controller.GameController;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.cellmap.CellMap;
import homemade.game.model.cellmap.CellMapReader;
import homemade.game.model.combo.ComboDetector;
import homemade.game.model.selection.BlockSelection;
import homemade.game.model.spawn.SpawnManager;

import java.util.HashMap;
import java.util.Map;

public class GameModelLinker
{
    private FieldStructure structure;
    private CellStates cellStates;

    private CellMap cellMap;
    private SpawnManager spawner;
    private ArrayBasedGameState state;

    private GameController controller;

    private GameState lastGameState;
    private BlockSelection selection;

    private GameSettings settings;

    private Updater updater;

    GameModelLinker(FieldStructure structure, GameSettings settings, GameController controller)
    {
        initialize(structure, settings, controller);
    }

    private synchronized void initialize(FieldStructure structure, GameSettings settings, GameController controller)
    {
        this.controller = controller;
        this.structure = structure;
        this.settings = settings;

        cellStates = new CellStates(structure.getFieldSize());

        BlockPool blockPool = new BlockPool(structure.getFieldSize(), cellStates);
        cellMap = new CellMap(structure, cellStates);

        state = new ArrayBasedGameState(structure, cellStates);
        lastGameState = state.getImmutableCopy();

        ComboDetector comboDetector = new ComboDetector(this, controller);
        GameScore gameScore = new GameScore(structure, controller, settings);

        updater = new Updater(this, comboDetector, cellMap, gameScore, blockPool, state);

        spawner = new SpawnManager(this, blockPool);
        selection = new BlockSelection(this);

        GameMode mode = settings.gameMode();
        if (mode == GameMode.TURN_BASED)
        {
            for (int i = 0; i < 2; i++)
                requestSpawn();
        }
    }

    synchronized public FieldStructure getStructure()
    {
        return structure;
    }
    synchronized public GameSettings getSettings()
    {
        return settings;
    }
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
        updater.takeChanges(spawner.spawnDeadBlocks());
        updateStates();
    }

    synchronized public void requestSpawn()
    {
        updater.takeComboChanges(spawner.spawnBlocks());

        Map<CellCode, CellState> marks = spawner.markCellsForSpawn();
        if (marks.size() == 0)
        {
            stopAllFacilities();
        }
        else
        {
            updater.takeChanges(marks);
        }

        updateStates();
    }

    synchronized public void tryMove(CellCode moveFromCell, CellCode moveToCell)
    {
        boolean riskOfSpawnDenial = false;

        if (cellMap.getCell(moveToCell).type() == Cell.MARKED_FOR_SPAWN)
            riskOfSpawnDenial = true;

        CellState cellFrom = cellMap.getCell(moveFromCell);
        CellState cellTo = cellMap.getCell(moveToCell);

        if (cellTo.isFreeForMove() && cellFrom.isOccupiedByBlock())
        {
            if (riskOfSpawnDenial)
                state.incrementDenyCounter();

            Map<CellCode, CellState> tmpMap = new HashMap<>();
            tmpMap.put(moveFromCell, cellStates.getState(riskOfSpawnDenial ? Cell.DEAD_BLOCK : Cell.EMPTY));
            tmpMap.put(moveToCell, cellFrom);

            updater.takeComboChanges(tmpMap);

            if (settings.gameMode() == GameMode.TURN_BASED && !updater.hasCombos())
            {
                requestSpawn();
            }
            else
            {
                updateStates();
            }
        }
    }

    private void updateStates()
    {
        if (updater.hasCellChanges())
        {
            selection.updateSelectionState();
            updater.flush();
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
}
