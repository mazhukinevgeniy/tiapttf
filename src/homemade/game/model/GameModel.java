package homemade.game.model;

import homemade.game.GameSettings;
import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.controller.GameController;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;

/**
 * GameModel handles commands from GameController.
 */
public class GameModel
{
    private GameModelLinker linker;

    public GameModel(GameController gameController, FieldStructure structure, GameSettings settings)
    {
        linker = new GameModelLinker(structure, settings, gameController);
    }

    public GameState copyGameState()
    {
        return linker.copyGameState();
    }

    public SelectionState copySelectionState()
    {
        return linker.getSelection().getSelectionState();
    }


    public void toggleSpawnPause()
    {
        linker.togglePause();
    }

    public void killRandomBlocks()
    {
        linker.killRandomBlocks();
    }


    public void tryToActivateCell(CellCode cell)
    {
        linker.getSelection().activateCell(cell);
    }

    public void tryMove(Direction direction)
    {
        linker.getSelection().tryToMoveSelectionIn(direction);
    }
}