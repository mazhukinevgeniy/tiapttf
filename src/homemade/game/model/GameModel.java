package homemade.game.model;

import homemade.game.GameSettings;
import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.controller.GameController;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.selection.BlockSelection;

/**
 * GameModel handles commands from GameController.
 */
public class GameModel
{
    private GameModelLinker linker;
    private BlockSelection selection;

    public GameModel(GameController gameController, FieldStructure structure, GameSettings settings)
    {
        linker = new GameModelLinker(structure, settings, gameController);

        selection = new BlockSelection(linker);
    }

    public GameState copyGameState()
    {
        return linker.copyGameState();
    }

    public SelectionState copySelectionState()
    {
        return selection.getSelectionState();
    }

    public void toggleSpawnPause()
    {
        linker.togglePause();
    }

    public void tryToActivateCell(CellCode cell)
    {
        selection.activateCell(cell);
    }

    public void tryMove(Direction direction)
    {
        selection.tryToMoveSelectionIn(direction);
    }
}