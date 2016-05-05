package homemade.utils.timer;

/**
 * Implement this interface to use QuickTimer.
 */
public interface TimerTaskPerformer
{
    void handleTimerTask();

    /**
     * Use this if your timer task performer needs to manipulate timer for some reason
     */
    default void setTimer(QuickTimer timer)
    {

    }

    abstract class TimerAwarePerformer implements TimerTaskPerformer
    {
        protected QuickTimer timer;

        @Override
        public void setTimer(QuickTimer timer)
        {
            this.timer = timer;
        }
    }
}
