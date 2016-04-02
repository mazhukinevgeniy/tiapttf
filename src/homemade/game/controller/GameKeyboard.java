package homemade.game.controller;

import homemade.game.Direction;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by user3 on 31.03.2016.
 */
class GameKeyboard implements KeyboardInputHandler
{
    private ArrayList<Integer> keyCodesPressed;

    /**
     * set of keyCodes we store
     */
    private HashSet<Integer> pressableKeys;

    /**
     * set of keyCodes which were actually used as an input
     * it's supposed to guarantee that every button press results in something if it should
     */
    private HashSet<Integer> readKeys;

    /**
     * set of keyCodes which were released
     */
    private HashSet<Integer> releasedKeys;

    GameController controller;

    GameKeyboard(GameController controller)
    {
        this.controller = controller;

        pressableKeys = new HashSet<Integer>(4);

        pressableKeys.add(KeyEvent.VK_UP);
        pressableKeys.add(KeyEvent.VK_DOWN);
        pressableKeys.add(KeyEvent.VK_LEFT);
        pressableKeys.add(KeyEvent.VK_RIGHT);
        pressableKeys.add(KeyEvent.VK_W);
        pressableKeys.add(KeyEvent.VK_A);
        pressableKeys.add(KeyEvent.VK_S);
        pressableKeys.add(KeyEvent.VK_D);


        int maxKeys = pressableKeys.size();
        keyCodesPressed = new ArrayList<Integer>(maxKeys);
        readKeys = new HashSet<Integer>(maxKeys);
        releasedKeys = new HashSet<Integer>(maxKeys);
    }

    Direction keyCodeToDirection(int keyCode)
    {
        Direction retVal = null;

        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W)
            retVal = Direction.TOP;
        else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S)
            retVal = Direction.BOTTOM;
        else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A)
            retVal = Direction.LEFT;
        else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D)
            retVal = Direction.RIGHT;

        return retVal;
    }

    synchronized int extractKey()
    {
        int size = keyCodesPressed.size();

        int key = 0; //TODO: find safe named return value

        if (size != 0)
        {
            key = keyCodesPressed.get(size - 1);
        }

        Iterator<Integer> iterator = keyCodesPressed.iterator();

        while (iterator.hasNext())
        {
            int keyCode = iterator.next();

            if (releasedKeys.contains(keyCode))
            {
                iterator.remove();
                releasedKeys.remove(keyCode);
            }
        }

        readKeys.addAll(keyCodesPressed); //every remaining keyCode was read at least once anyway

        return key;
    }

    @Override
    synchronized public void keyPressed(int keyCode)
    {
        if (pressableKeys.contains(keyCode))
        {
            tryToRemoveFromPressed(keyCode);

            keyCodesPressed.add(keyCode);
            readKeys.remove(keyCode);
        }
    }

    @Override
    synchronized public void keyReleased(int keyCode)
    {
        if (pressableKeys.contains(keyCode))
        {
            if (readKeys.contains(keyCode))
            {
                tryToRemoveFromPressed(keyCode);
                readKeys.remove(keyCode);
            }
            else
                releasedKeys.add(keyCode);
        }
    }

    private void tryToRemoveFromPressed(int keyCode)
    {
        int keyCodePosition = keyCodesPressed.indexOf(keyCode);

        if (keyCodePosition != -1)
            keyCodesPressed.remove(keyCodePosition);
    }
}
