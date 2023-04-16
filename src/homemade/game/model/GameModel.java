package homemade.game.model;

import homemade.game.GameSettings;
import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.controller.GameController;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.loop.GameLoop;

/**
 * GameModel handles commands from GameController.
 * //TODO remove
 */
public class GameModel {
    private GameModelLinker linker; // todo: obsoleted by gameloop

    public GameModel(GameController gameController, FieldStructure structure, GameSettings settings, GameLoop gameLoop) {
        linker = new GameModelLinker(structure, settings, gameController, gameLoop);
    }

    public GameState copyGameState() {
        return linker.copyGameState();
    }

    public SelectionState copySelectionState() {
        return linker.getSelection().getSelectionState();
    }


    public void toggleSpawnPause() {
        linker.togglePause();
    }


    public void tryToActivateCell(CellCode cell) {
        linker.getSelection().activateCell(cell);
    }

    public void tryMove(Direction direction) {
        linker.getSelection().tryToMoveSelectionIn(direction);
    }
}
