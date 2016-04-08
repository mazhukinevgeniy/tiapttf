package homemade.game.model;

import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.controller.GameController;
import homemade.game.model.cellmap.CellMap;
import homemade.game.model.combo.ComboDetector;
import homemade.game.model.spawn.SpawnManager;

/**
 * Created by user3 on 22.03.2016.
 */
public class GameModel
{
    private GameState gameState;

    private SpawnManager spawner;
    private GameModelLinker linker;

    public GameModel(GameController gameController)
    {
        NumberPool numberPool = new NumberPool(Game.FIELD_SIZE);

        ArrayBasedGameState gameStateTracker = new ArrayBasedGameState();
        CellMap cellMap = new CellMap();

        ComboDetector comboDetector = ComboDetector.initializeComboDetection(cellMap, gameController);

        GameStats stats = new GameStats();


        gameState = gameStateTracker;

        linker = new GameModelLinker(cellMap, comboDetector, numberPool, gameStateTracker);

        spawner = new SpawnManager(linker, cellMap, numberPool, stats);


    }

    public GameState copyGameState()
    {
        return gameState.getImmutableCopy();
    }

    public SpawnManager getPauseToggler()
    {
        return spawner;
    }

    public GameModelLinker getMoveRequestHandler()
    {
        return linker;
        //TODO: fix terrible gap between the method name and the type returned
    }


}
