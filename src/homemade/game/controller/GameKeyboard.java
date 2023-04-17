package homemade.game.controller;

import homemade.game.loop.EventPoster;
import homemade.game.loop.GameEvent;
import homemade.game.loop.PauseToggle;

import java.awt.event.KeyEvent;

class GameKeyboard implements KeyboardInputHandler {

    private EventPoster<GameEvent> eventPoster;

    GameKeyboard(EventPoster<GameEvent> eventPoster) {
        this.eventPoster = eventPoster;
    }

    @Override
    public void keyPressed(int keyCode) {
    }

    @Override
    public void keyReleased(int keyCode) {
        if (keyCode == KeyEvent.VK_SPACE) {
            eventPoster.post(PauseToggle.INSTANCE);
        }
    }
}
