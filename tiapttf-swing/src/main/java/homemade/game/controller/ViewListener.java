package homemade.game.controller;

public final class ViewListener {
    private MouseInputHandler mouse;
    private KeyboardInputHandler keyboard;

    private GameController gameController;

    ViewListener(GameController gameController, MouseInputHandler mouseInputHandler, KeyboardInputHandler keyboardInputHandler) {
        this.gameController = gameController;

        mouse = mouseInputHandler;
        keyboard = keyboardInputHandler;
    }

    public MouseInputHandler mouseInputHandler() {
        return mouse;
    }

    public KeyboardInputHandler keyboardInputHandler() {
        return keyboard;
    }

    public void quit() {
        gameController.requestQuit();
    }
}
