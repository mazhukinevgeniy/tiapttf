package homemade.game.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class FpsTracker {
    private static long nanosPerSecond = TimeUnit.MILLISECONDS.toNanos(1000);

    private List<Long> times;

    FpsTracker() {
        times = new LinkedList<>();
    }

    /**
     * People say nanoTime could be unstable in multi-core environment, use this method with caution
     */
    void addTimestamp() {
        long newTime = System.nanoTime();

        times.add(newTime);

        while (newTime - times.get(0) > nanosPerSecond) {
            times.remove(0);
        }

        long divider;
        int frames = times.size();

        if (frames < 2)
            divider = nanosPerSecond;
        else
            divider = newTime - times.get(0);

        long fps = ((frames - 1) * nanosPerSecond) / divider;

        if (new Random().nextInt(100) < 2) {
            System.out.println("fps is " + fps);
        }
    }
}
