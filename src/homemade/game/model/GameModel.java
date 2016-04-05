package homemade.game.model;

import homemade.game.CellCode;
import homemade.game.Direction;
import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.controller.GameController;
import homemade.game.model.cellmap.Cell;
import homemade.game.model.cellmap.CellMap;
import homemade.game.model.cellmap.Link;
import homemade.utils.QuickMap;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user3 on 22.03.2016.
 */
public class GameModel
{
    private GameState gameState;

    private CellMap cellMap;
    private NumberPool numberPool;

    private ComboDetector comboDetector;
    private BlockSpawner spawner;
    private Timer timer;

    ArrayBasedGameState gameStateTracker;

    private boolean paused = false;

    public GameModel(GameController gameController)
    {
        numberPool = new NumberPool(Game.FIELD_WIDTH * Game.FIELD_HEIGHT);
        GameScore gameScore = new GameScore(gameController);

        gameStateTracker = new ArrayBasedGameState();
        cellMap = new CellMap();
        comboDetector = new ComboDetector(cellMap, gameController, gameScore);

        gameState = gameStateTracker;

        spawner = new BlockSpawner(cellMap, numberPool);

        this.timer = new Timer();

        long delay = 0;
        long period = Game.SPAWN_PERIOD;
        this.timer.schedule(new GameTimerTask(this), delay, period);
        //TODO: measure and show the actual period

    }

    void handleTimerTask()
    {
        if (!paused)
        {
            Map<CellCode, Integer> changes = spawner.spawnBlocks();
            changes.putAll(spawner.markCells(Game.SIMULTANEOUS_SPAWN));
            //can do because first call doesn't interfere with the second

            Set<CellCode> appliedChanges = cellMap.applyCascadeChanges(changes);
            actOnChangedCells(appliedChanges);
        }
    }

    public GameState copyGameState()
    {
        return gameState.getImmutableCopy();
    }

    public void requestPauseToggle()
    {
        paused = !paused;
    }

    public void gameOver()
    {
        this.timer.cancel();
        this.timer.purge();
    }

    public void blockMoveRequested(CellCode cellCodeFrom, CellCode cellCodeTo)
    {
        Set<CellCode> changes = cellMap.tryCascadeChanges(cellCodeFrom, cellCodeTo);
        actOnChangedCells(changes);
    }

    private void actOnChangedCells(Set<CellCode> changedCells)
    {
        if (changedCells.size() > 0)
        {
            if (Game.AUTOCOMPLETION)
            {
                Map<CellCode, Integer> removedCells = QuickMap.getCleanCellCodeIntMap();

                Set<CellCode> cellsToRemove = comboDetector.findCellsToRemove(changedCells);

                for (CellCode cellCode : cellsToRemove)
                {
                    numberPool.freeNumber(cellMap.getCell(cellCode).getValue());

                    removedCells.put(cellCode, Game.CELL_EMPTY);
                }

                cellMap.applyCascadeChanges(removedCells);

                Set<CellCode> removedKeys = removedCells.keySet();
                for (CellCode key : removedKeys)
                {
                    changedCells.add(key);
                }
            }

            Map<CellCode, Integer> updatedCells = QuickMap.getCleanCellCodeIntMap();
            Map<Integer, Boolean> updatedLinks = QuickMap.getCleanIntBoolMap();

            for (CellCode cellCode : changedCells)
            {
                Cell cell = cellMap.getCell(cellCode);

                updatedCells.put(cellCode, cell.getValue());

                for (Direction direction : Direction.values())
                {
                    Link link = cell.link(direction);

                    if (link != null)
                    {
                        updatedLinks.put(link.getNumber(), link.getValue());
                    }
                }
            }

            gameStateTracker.updateFieldSnapshot(updatedCells, updatedLinks);
        }
    }


    private class GameTimerTask extends TimerTask
    {
        private GameModel model;

        GameTimerTask(GameModel model)
        {
            this.model = model;
        }

        @Override
        public void run()
        {
            this.model.handleTimerTask();
            //I guess it's ok to have no logic here because we might want to replace timer
        }
    }
}
