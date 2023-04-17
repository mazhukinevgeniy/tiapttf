package homemade.game.controller;

import java.awt.event.KeyEvent;

class GameKeyboard implements KeyboardInputHandler {

    private GameController controller;

    GameKeyboard(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void keyPressed(int keyCode) {
    }

    @Override
    synchronized public void keyReleased(int keyCode) {
        if (keyCode == KeyEvent.VK_SPACE) {
            controller.requestPauseToggle();
        }
    }
}
