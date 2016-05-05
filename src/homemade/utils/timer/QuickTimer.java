package homemade.utils.timer;


import java.util.Timer;
import java.util.TimerTask;

public class QuickTimer
{
    private Timer timer;

    private TimerTaskPerformer performer;
    private long period;

    private boolean stopped = false;

    public QuickTimer(TimerTaskPerformer performer, long period)
    {
        this(performer, period, 0);
    }

    public QuickTimer(TimerTaskPerformer performer, long period, long delay)
    {
        this.performer = performer;
        this.period = period;

        performer.setTimer(this);

        timer = new Timer();
        timer.schedule(new SynchronizedTask(), delay, period);
    }

    private synchronized void runSynchronized()
    {
        //System.out.println(this.toString() + " " + stopped);

        if (!stopped)
        {
            performer.handleTimerTask();
        }
    }

    public synchronized void stop()
    {
        timer.cancel();
        stopped = true;
    }

    /**
     * Care: this will cancel all tasks if period is actually changed.
     */
    public synchronized void setPeriod(long newPeriod)
    {
        if (!stopped && newPeriod != period)
        {
            period = newPeriod;

            timer.cancel();
            timer = new Timer();
            timer.schedule(new SynchronizedTask(), period, period);
            //TODO: separate quicktimer and variableperiodtimer so that inner quicktimer is truly stopped when period changes
        }
    }

    private class SynchronizedTask extends TimerTask
    {
        @Override
        public void run()
        {
            runSynchronized();
        }
    }
}
