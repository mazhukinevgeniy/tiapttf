package homemade.game.model;

import homemade.game.ExtendedGameState;
import homemade.game.GameSettings;
import homemade.game.GameSettings.GameMode;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.loop.GameLoop;
import homemade.game.loop.GameOver;
import homemade.game.loop.MultiplierChanged;
import homemade.game.model.cellmap.CellMap;
import homemade.game.model.cellmap.CellMapReader;
import homemade.game.model.cellstates.SimpleState;
import homemade.game.model.combo.ComboDetector;
import homemade.game.model.combo.ComboEffectVendor;
import homemade.game.model.selection.BlockSelection;
import homemade.game.model.spawn.SpawnManager;
import homemade.game.scenarios.GameOverScenario;
import homemade.game.scenarios.RealtimeSpawningScenario;
import homemade.game.scenarios.SnapshotRequestScenario;
import homemade.game.scenarios.UserInputScenario;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GameModelLinker {
    private static final int BONUS_MULTIPLIER_FOR_BOARD_CLEAR = 50;
    private static final int INITIAL_SPAWNS = 2;

    private FieldStructure structure;

    private CellMap cellMap;
    private SpawnManager spawner;
    public ArrayBasedGameState state;
    private LinkedList<ComboEffect> storedEffects;

    private GameLoop gameLoop;

    public ExtendedGameState lastGameState;
    public BlockSelection selection;

    private GameSettings settings;

    private Updater updater;

    public GameModelLinker(FieldStructure structure, GameSettings settings, GameLoop gameLoop) {
        this.structure = structure;
        this.settings = settings;
        this.gameLoop = gameLoop;

        //new FieldUpdatePipeline(gameLoop, new MutableGameState(structure));


        BlockValuePool blockValuePool = new BlockValuePool(settings.getMaxBlockValue(), structure.fieldSize);
        cellMap = new CellMap(structure, blockValuePool);

        storedEffects = new LinkedList<>();

        state = new ArrayBasedGameState(structure);

        ComboDetector comboDetector = new ComboDetector(this, gameLoop.getUi());
        GameScore gameScore = new GameScore(this);

        updater = new Updater(this, comboDetector, cellMap, gameScore, state);

        spawner = new SpawnManager(this, blockValuePool);
        selection = new BlockSelection(this);

        lastGameState = new ExtendedGameState(state.createImmutableCopy(), selection.copySelectionState());

        GameMode mode = settings.getGameMode();
        if (mode == GameMode.TURN_BASED) {
            updater.takeChanges(spawner.markCellsForSpawn());

            for (int i = 0; i < INITIAL_SPAWNS; i++) {
                requestSpawn();
            }
        } else {
            new RealtimeSpawningScenario(this, settings, gameLoop);
        }

        new GameOverScenario(gameLoop, this);
        new SnapshotRequestScenario(gameLoop, this);
        new UserInputScenario(gameLoop, selection);
    }

    public FieldStructure getStructure() {
        return structure;
    }

    public GameSettings getSettings() {
        return settings;
    }

    synchronized public CellMapReader getMapReader() {
        return cellMap;
    }

    public synchronized void killRandomBlocks() {
        updater.takeChanges(spawner.spawnDeadBlocks());
        updateStates();
    }

    synchronized void updateScore(int newScore) {
        state.updateScore(newScore);
    }

    synchronized void modifyGlobalMultiplier(int change) {
        int oldMultiplier = state.getGlobalMultiplier();
        int rawMultiplier = oldMultiplier + change;
        int newMultiplier = Math.max(1, rawMultiplier);

        if (oldMultiplier != newMultiplier) {
            state.updateMultiplier(newMultiplier);
            gameLoop.getUi().post(new MultiplierChanged(change));
        }
    }

    synchronized public void requestSpawn() {
        modifyGlobalMultiplier(-1);

        updater.takeComboChanges(spawner.spawnBlocks());
        updater.takeChanges(spawner.markCellsForSpawn());

        updateStates();
    }

    public void tryMove(CellCode moveFromCell, CellCode moveToCell) {
        boolean repercussions = cellMap.getCell(moveToCell).type() == Cell.MARKED_FOR_SPAWN &&
                state.getGlobalMultiplier() == 1;

        CellState cellFrom = cellMap.getCell(moveFromCell);
        CellState cellTo = cellMap.getCell(moveToCell);

        if (cellTo.isFreeForMove() && cellFrom.isMovableBlock()) {
            state.incrementDenyCounter();

            Map<CellCode, CellState> tmpMap = new HashMap<>();
            tmpMap.put(moveFromCell, SimpleState.getSimpleState(repercussions ? Cell.DEAD_BLOCK : Cell.EMPTY));
            tmpMap.put(moveToCell, cellFrom);

            updater.takeComboChanges(tmpMap);

            if (settings.getGameMode() == GameMode.TURN_BASED && !updater.hasCombos()) {
                requestSpawn();
            } else {
                updateStates();
            }
        }
    }

    private void updateStates() {
        if (updater.hasCellChanges()) {
            int comboPackTier = updater.comboPackTier();
            new ComboEffectVendor().addComboEffectsForTier(storedEffects, comboPackTier);

            updater.takeChanges(spawner.markBlocksWithEffects(storedEffects));

            selection.updateSelectionState();
            updater.flush();
        }

        if (state.getNumberOfBlocks() == structure.fieldSize) {
            int multiplier = state.getGlobalMultiplier();

            if (multiplier > 1) {
                modifyGlobalMultiplier(-multiplier);

                updater.takeChanges(spawner.removeRandomBlocks());
                updateStates();
                System.out.println("multiplier consumed");
            } else {
                gameLoop.getModel().post(new GameOver(1));
                System.out.println("can't trade multiplier for blocks");
            }
        } else if (state.getNumberOfMovableBlocks() == 0) {
            modifyGlobalMultiplier(BONUS_MULTIPLIER_FOR_BOARD_CLEAR + INITIAL_SPAWNS);

            for (int i = 0; i < INITIAL_SPAWNS; i++) {
                requestSpawn();
            }
        }
    }

    ExtendedGameState copyGameState() {
        return lastGameState;
    }
}
