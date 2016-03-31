package homemade.game.controller;

import homemade.game.CellCode;
import homemade.game.Game;
import homemade.game.SelectionState;
import homemade.game.model.GameModel;
import homemade.game.view.GameView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user3 on 23.03.2016.
 */
public class GameController
{
    GameModel model;
    private GameView view;

    private SelectionManager selectionManager;
    private GameKeyboard keyboard;

    public GameController(Frame mainFrame)
    {
        CellCode.initializeCellCodes();

        model = new GameModel();

        selectionManager = new SelectionManager(this);
        keyboard = new GameKeyboard(this);

        view = new GameView(this, mainFrame);

        Timer timer = new Timer();
        long delay = 0;
        long period = 1000 / Game.KEY_INPUT_CAP_PER_SECOND;
        timer.schedule(new ControllerTimerTask(this), delay, period);
    }

    public MouseInputHandler mouseInputHandler() { return selectionManager; }
    public KeyboardInputHandler keyboardInputHandler() { return keyboard; }

    public void viewTimerUpdated()
    {
        this.view.renderNextFrame(model.copyGameState(), selectionManager.getSelectionState());
    }

    void controllerTimerUpdated()
    {


        //int keyCode = keyboard.extractKey();

        //if (keyCodeToCellCodeShift.containsKey(keyCode))
        //    selectionManager.tryToMoveSelectionIn(keyCodeToCellCodeShift.get(keyCode));
    }
    //TODO: this method is literally empty, do something

    void relevantKeyCodeReleased(int keyCode)
    {
        HashMap<Integer, Integer> keyCodeToCellCodeShift = new HashMap<Integer, Integer>();

        keyCodeToCellCodeShift.put(KeyEvent.VK_LEFT, -1);
        keyCodeToCellCodeShift.put(KeyEvent.VK_RIGHT, 1);
        keyCodeToCellCodeShift.put(KeyEvent.VK_UP, -Game.FIELD_WIDTH);
        keyCodeToCellCodeShift.put(KeyEvent.VK_DOWN, Game.FIELD_WIDTH);

        if (keyCodeToCellCodeShift.containsKey(keyCode))
            selectionManager.tryToMoveSelectionIn(keyCodeToCellCodeShift.get(keyCode));
    }
    //TODO: this code is a bit out of place isn't it, fix it


    private class ControllerTimerTask extends TimerTask
    {
        private GameController controller;

        ControllerTimerTask(GameController controller)
        {
            this.controller = controller;
        }

        @Override
        public void run()
        {
            this.controller.controllerTimerUpdated();
        }
    }
    //TODO: think about this: now everyone has a timer, is it right?
    //TODO: clean the mess caused by adding keyboard input at 2 AM
}
