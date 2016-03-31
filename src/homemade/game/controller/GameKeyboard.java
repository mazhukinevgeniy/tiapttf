package homemade.game.controller;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by user3 on 31.03.2016.
 */
class GameKeyboard implements KeyboardInputHandler
{
    private ArrayList<Integer> keyCodesPressed;

    private HashSet<Integer> keyCodesReleased;
    private HashSet<Integer> pressableKeys;

    GameController controller;

    GameKeyboard(GameController controller)
    {
        this.controller = controller;

        pressableKeys = new HashSet<Integer>(4);

        pressableKeys.add(KeyEvent.VK_UP);
        pressableKeys.add(KeyEvent.VK_DOWN);
        pressableKeys.add(KeyEvent.VK_LEFT);
        pressableKeys.add(KeyEvent.VK_RIGHT);

        keyCodesPressed = new ArrayList<Integer>(pressableKeys.size());
        keyCodesReleased = new HashSet<>(pressableKeys.size());
    }
/*
    synchronized int extractKey()
    {
        int size = keyCodesPressed.size();

        int key = 0; //TODO: find safe named return value

        if (size != 0)
        {
            key = keyCodesPressed.get(size - 1);

            keyCodesPressed.removeAll(keyCodesReleased);
            keyCodesReleased.clear();
        }

        return key;
    }*/
    //TODO: try to fix this. meanwhile, why don't we just use controller.relevantKeyCodeReleased

    @Override
    synchronized public void keyPressed(int keyCode)
    {
        if (pressableKeys.contains(keyCode))
        {
            tryToRemoveFromPressed(keyCode);

            keyCodesPressed.add(keyCode);
        }
    }

    @Override
    synchronized public void keyReleased(int keyCode)
    {
        if (pressableKeys.contains(keyCode))
        {
            keyCodesReleased.add(keyCode);

            controller.relevantKeyCodeReleased(keyCode);
        }
    }

    private void tryToRemoveFromPressed(int keyCode)
    {
        int keyCodePosition = keyCodesPressed.indexOf(keyCode);

        if (keyCodePosition != -1)
            keyCodesPressed.remove(keyCodePosition);
    }
}
