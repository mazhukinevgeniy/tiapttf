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

        BlockValuePool blockValuePool = new BlockValuePool(structure.getFieldSize());
        cellMap = new CellMap(structure, blockValuePool);

        state = new ArrayBasedGameState(structure);
        lastGameState = state.getImmutableCopy();

        ComboDetector comboDetector = new ComboDetector(this, controller);
        GameScore gameScore = new GameScore(this);

        updater = new Updater(this, comboDetector, cellMap, gameScore, state);

        spawner = new SpawnManager(this, blockValuePool);
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

    synchronized BlockSelection getSelection()
    {
        return selection;
    }

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

    synchronized void updateScore(int newScore)
    {
        state.updateScore(newScore);
    }

    synchronized void modifyGlobalMultiplier(int change)
    {
        int oldMultiplier = state.globalMultiplier();
        int rawMultiplier = oldMultiplier + change;
        int newMultiplier = Math.max(1, rawMultiplier);

        if (oldMultiplier != newMultiplier)
        {
            state.updateMultiplier(newMultiplier);
            controller.multiplierChanged(change);
        }
    }

    synchronized public void requestSpawn()
    {
        modifyGlobalMultiplier(-1);

        updater.takeComboChanges(spawner.spawnBlocks());
        updater.takeChanges(spawner.markCellsForSpawn());

        updateStates();

        if (state.numberOfBlocks() == structure.getFieldSize())
        {
            int multiplier = state.globalMultiplier();

            if (multiplier > 1)
            {
                modifyGlobalMultiplier(-multiplier);

                updater.takeChanges(spawner.removeRandomBlocks());
                updateStates();
                System.out.println("multiplier consumed");
            }
            else
            {
                stopAllFacilities();
                System.out.println("no multiplier");
            }
        }
    }

    synchronized public void tryMove(CellCode moveFromCell, CellCode moveToCell)
    {
        boolean repercussions = cellMap.getCell(moveToCell).type() == Cell.MARKED_FOR_SPAWN &&
                                state.globalMultiplier() == 1;

        CellState cellFrom = cellMap.getCell(moveFromCell);
        CellState cellTo = cellMap.getCell(moveToCell);

        if (cellTo.isFreeForMove() && cellFrom.isMovable())
        {
            if (repercussions)
                state.incrementDenyCounter();

            Map<CellCode, CellState> tmpMap = new HashMap<>();
            tmpMap.put(moveFromCell, CellState.simpleState(repercussions ? Cell.DEAD_BLOCK : Cell.EMPTY));
            tmpMap.put(moveToCell, cellFrom);

            updater.takeComboChanges(tmpMap);

            if (settings.gameMode() == GameMode.TURN_BASED && !updater.hasCombos())
            {
                requestSpawn();
                //TODO: fix edge case when board is cleared in turn-based mode and the new move is impossible
                //it'd make sense to give player a ton of points and fill the board like in the beginning
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
            int comboPackTier = updater.comboPackTier();
            updater.takeChanges(spawner.markBlocksWithEffects(comboPackTier));

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
