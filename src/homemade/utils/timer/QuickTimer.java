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
        timer.schedule(new SmartTimerTask(), delay, period);
    }

    public void stop()
    {
        timer.cancel();
    }

    /**
     * Care: this will cancel all tasks if period is actually changed.
     */
    public void setPeriod(long newPeriod)
    {
        if (newPeriod != period)
        {
            period = newPeriod;

            timer.cancel();
            timer = new Timer();
            timer.schedule(new SmartTimerTask(), period, period);
        }
    }

    private class SmartTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            performer.handleTimerTask();
        }
    }
}
