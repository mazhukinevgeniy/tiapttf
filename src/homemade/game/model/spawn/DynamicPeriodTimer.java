package homemade.game.model.spawn;

import homemade.game.GameSettings;
import homemade.game.model.GameModelLinker;

class DynamicPeriodTimer implements SpawnTimer {
    private SpawnPeriod period;

    private GameModelLinker linker;

    private boolean paused = false;

    private int timeElapsed = 0;

    DynamicPeriodTimer(GameModelLinker linker, GameSettings settings) {
        this.linker = linker;

        period = SpawnPeriod.newFastStart(linker, settings);
    }

    @Override
    public void stop() {
        paused = true;
    }

    @Override
    public void toggleSpawnPause() {
        paused = !paused;
    }

    @Override
    public void timeElapsed(int time) {
        if (paused) {
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
