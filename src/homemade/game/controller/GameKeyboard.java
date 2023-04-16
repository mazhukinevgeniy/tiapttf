package homemade.game.controller;

import homemade.game.fieldstructure.Direction;

import java.awt.event.KeyEvent;
import java.util.*;

class GameKeyboard implements KeyboardInputHandler {
    private static Map<Integer, Direction> keyToDirection;

    static {
        keyToDirection = new HashMap<>(9);

        keyToDirection.put(KeyEvent.VK_UP, Direction.TOP);
        keyToDirection.put(KeyEvent.VK_W, Direction.TOP);

        keyToDirection.put(KeyEvent.VK_DOWN, Direction.BOTTOM);
        keyToDirection.put(KeyEvent.VK_S, Direction.BOTTOM);

        keyToDirection.put(KeyEvent.VK_LEFT, Direction.LEFT);
        keyToDirection.put(KeyEvent.VK_A, Direction.LEFT);

        keyToDirection.put(KeyEvent.VK_RIGHT, Direction.RIGHT);
        keyToDirection.put(KeyEvent.VK_D, Direction.RIGHT);

        keyToDirection.put(KeyEvent.VK_UNDEFINED, null);
    }


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

    private GameController controller;

    GameKeyboard(GameController controller) {
        this.controller = controller;

        pressableKeys = new HashSet<>(8);

        pressableKeys.add(KeyEvent.VK_UP);
        pressableKeys.add(KeyEvent.VK_DOWN);
        pressableKeys.add(KeyEvent.VK_LEFT);
        pressableKeys.add(KeyEvent.VK_RIGHT);
        pressableKeys.add(KeyEvent.VK_W);
        pressableKeys.add(KeyEvent.VK_A);
        pressableKeys.add(KeyEvent.VK_S);
        pressableKeys.add(KeyEvent.VK_D);


        int maxKeys = pressableKeys.size();
        keyCodesPressed = new ArrayList<>(maxKeys);
        readKeys = new HashSet<>(maxKeys);
        releasedKeys = new HashSet<>(maxKeys);
    }

    synchronized Direction extractDirection() {
        //TODO remove
        int size = keyCodesPressed.size();
        int key = KeyEvent.VK_UNDEFINED;

        if (size != 0) {
            key = keyCodesPressed.get(size - 1);
        }

        handleKeyExtraction();

        return keyToDirection.get(key);
    }

    private void handleKeyExtraction() {
        Iterator<Integer> iterator = keyCodesPressed.iterator();

        while (iterator.hasNext()) {
            int keyCode = iterator.next();

            if (releasedKeys.contains(keyCode)) {
                iterator.remove();
                releasedKeys.remove(keyCode);
            }
        }

        readKeys.addAll(keyCodesPressed); //every remaining keyCode was read at least once anyway
    }

    @Override
    synchronized public void keyPressed(int keyCode) {
        if (pressableKeys.contains(keyCode)) {
            tryToRemoveFromPressed(keyCode);

            keyCodesPressed.add(keyCode);
            readKeys.remove(keyCode);
        }
    }

    @Override
    synchronized public void keyReleased(int keyCode) {
        if (pressableKeys.contains(keyCode)) {
            if (readKeys.contains(keyCode)) {
                tryToRemoveFromPressed(keyCode);
                readKeys.remove(keyCode);
            } else
                releasedKeys.add(keyCode);
        } else if (keyCode == KeyEvent.VK_SPACE) {
            controller.requestPauseToggle();
        }
    }

    private void tryToRemoveFromPressed(int keyCode) {
        int keyCodePosition = keyCodesPressed.indexOf(keyCode);

        if (keyCodePosition != -1)
            keyCodesPressed.remove(keyCodePosition);
    }
}
