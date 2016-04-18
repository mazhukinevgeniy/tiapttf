package homemade.game.model;

import homemade.game.GameState;
import homemade.game.controller.GameController;
import homemade.game.fieldstructure.FieldStructure;
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

    public GameModel(GameController gameController, FieldStructure structure)
    {
        NumberPool numberPool = new NumberPool(structure.getFieldSize());

        ArrayBasedGameState gameStateTracker = new ArrayBasedGameState(structure);
        CellMap cellMap = new CellMap(structure);

        ComboDetector comboDetector = ComboDetector.initializeComboDetection(structure, cellMap, gameController);


        gameState = gameStateTracker;

        linker = new GameModelLinker(structure, cellMap, comboDetector, numberPool, gameStateTracker);

        spawner = new SpawnManager(linker, cellMap, numberPool);


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
