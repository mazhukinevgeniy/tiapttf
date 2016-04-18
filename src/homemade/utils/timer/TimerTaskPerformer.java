package homemade.utils.timer;

/**
 * Implement this interface to use QuickTimer.
 */
public interface TimerTaskPerformer
{
    public void handleTimerTask();

    /**
     * Use this if your timer task performer needs to manipulate timer for some reason
     */
    default void setTimer(QuickTimer timer)
    {

    }
}
