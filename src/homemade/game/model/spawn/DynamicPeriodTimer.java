package homemade.game.model.spawn;

import homemade.game.GameSettings;
import homemade.game.model.GameModelLinker;
import homemade.utils.timer.QuickTimer;
import homemade.utils.timer.TimerTaskPerformer;

class DynamicPeriodTimer implements SpawnTimer
{
    private SpawnPeriod period;
    private QuickTimer timer;

    private GameModelLinker linker;

    private boolean paused = false;

    DynamicPeriodTimer(GameModelLinker linker, GameSettings settings)
    {
        this.linker = linker;

        period = SpawnPeriod.newFastStart(linker, settings);


        long startingPeriod = period.getSpawnPeriod();
        timer = new QuickTimer(new SpawnTimerTaskPerformer(), startingPeriod, startingPeriod);
        //TODO: measure and show the actual period
    }

    @Override
    public void stop()
    {
        paused = true;

        timer.stop();
    }

    @Override
    public void toggleSpawnPause()
    {
        paused = !paused;
    }

    private class SpawnTimerTaskPerformer implements TimerTaskPerformer
    {
        @Override
        public void handleTimerTask()
        {
            System.out.println("spawn timer task " + this.toString());

            if (!paused)
            {
                timer.setPeriod(period.getSpawnPeriod());

                linker.requestSpawn();
            }
        }
    }
}