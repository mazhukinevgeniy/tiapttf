package homemade.game.controller;

/**
 * Created by user3 on 17.04.2016.
 */
public final class ViewListener
{
    private MouseInputHandler mouse;
    private KeyboardInputHandler keyboard;

    private GameController controller;

    ViewListener(GameController controller, MouseInputHandler mouseInputHandler, KeyboardInputHandler keyboardInputHandler)
    {
        this.controller = controller;

        mouse = mouseInputHandler;
        keyboard = keyboardInputHandler;
    }

    public void viewTimerUpdated()
    {
        controller.sendDataToRender();
    }

    public MouseInputHandler mouseInputHandler() { return mouse; }
    public KeyboardInputHandler keyboardInputHandler() { return keyboard; }
}
