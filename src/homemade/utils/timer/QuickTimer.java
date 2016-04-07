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
        timer = new Timer();
    }


    private void reschedule()
    {
        if (false) //TODO: add period modification
        {

        }
    }

    private class SmartTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            reschedule();
            performer.handleTimerTask();
        }
    }
}
