package homemade.game.model;

import homemade.game.GameState;
import homemade.game.controller.GameController;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.cellmap.CellMap;
import homemade.game.model.spawn.SpawnManager;

public class GameModel
{
    private SpawnManager spawner;
    private GameModelLinker linker;

    public GameModel(GameController gameController, FieldStructure structure)
    {
        NumberPool numberPool = new NumberPool(structure.getFieldSize());
        CellMap cellMap = new CellMap(structure);

        linker = new GameModelLinker(structure, cellMap, gameController, numberPool);

        spawner = new SpawnManager(linker, cellMap, numberPool);
    }

    public GameState copyGameState()
    {
        return linker.copyGameState();
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