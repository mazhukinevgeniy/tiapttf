package homemade.utils.timer;


import java.util.Timer;
import java.util.TimerTask;

public class QuickTimer
{
    private Timer timer;

    private TimerTaskPerformer performer;
    private long period;


    public QuickTimer(TimerTaskPerformer performer, long period)
    {
        this(performer, period, 0);
    }

    public QuickTimer(TimerTaskPerformer performer, long period, long delay)
    {
        this.performer = performer;
        this.period = period;


        timer = new Timer();
        timer.schedule(new SmartTimerTask(performer, this), delay, period);
    }

    public void stop()
    {
        timer.cancel();
        timer = new Timer();
    }


    private void reschedule()
    {
        if (false) //TODO: add period modification
        {

        }
    }

    private static class SmartTimerTask extends TimerTask
    {
        private TimerTaskPerformer performer;
        private QuickTimer quickTimer;

        SmartTimerTask(TimerTaskPerformer performer, QuickTimer quickTimer)
        {
            this.performer = performer;
            this.quickTimer = quickTimer;
        }

        @Override
        public void run()
        {
            quickTimer.reschedule();
            performer.handleTimerTask();
        }
    }
}
