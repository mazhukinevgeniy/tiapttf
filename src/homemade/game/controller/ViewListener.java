package homemade.game.controller;

/**
 * Created by user3 on 17.04.2016.
 */
public final class ViewListener
{
    private MouseInputHandler mouse;
    private KeyboardInputHandler keyboard;

    ViewListener(MouseInputHandler mouseInputHandler, KeyboardInputHandler keyboardInputHandler)
    {
        mouse = mouseInputHandler;
        keyboard = keyboardInputHandler;
    }

    public MouseInputHandler mouseInputHandler() { return mouse; }
    public KeyboardInputHandler keyboardInputHandler() { return keyboard; }
}
