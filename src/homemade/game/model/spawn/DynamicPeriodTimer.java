package homemade.game.model.spawn;

import homemade.game.GameSettings;
import homemade.game.loop.*;
import homemade.game.model.GameModelLinker;

public class DynamicPeriodTimer implements GameEventHandler<GameEvent> {
    private SpawnPeriod period;

    private GameModelLinker linker;

    private boolean paused = false;
    private boolean stopped = false;

    private int timeElapsed = 0;

    public DynamicPeriodTimer(GameModelLinker linker, GameSettings settings) {
        this.linker = linker;

        period = SpawnPeriod.newFastStart(linker, settings);
    }

    @Override
    public void handle(GameEvent event) {
        if (event instanceof GameOver) {
            stopped = true;
        } else if (event instanceof TimeElapsed) {
            timeElapsed(((TimeElapsed) event).getDiffMs());
        } else if (event instanceof PauseToggle) {
            paused = !paused;
        } else {
            System.err.println("unexpected event " + event);
            System.exit(1);
        }
    }

    private void timeElapsed(int time) {
        if (paused || stopped) {
            return;
        }
        timeElapsed += time;
        int timeRequired = (int) period.getSpawnPeriod();
        if (timeElapsed > timeRequired) {
            timeElapsed = 0;
            linker.requestSpawn();
        }
    }
}
