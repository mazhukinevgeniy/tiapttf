package homemade.game.model;

import java.util.LinkedList;
import java.util.Random;

public class BlockValuePool {
    private LinkedList<Integer> available;
    private Random random;

    BlockValuePool(int max) {
        available = new LinkedList<>();

        for (int i = 0; i < max; i++) {
            available.add(Integer.valueOf(i + 1));
        }

        random = new Random();
    }

    public Integer takeBlockValue() {
        int length = available.size();
        int position = random.nextInt(length);

        return available.remove(position);
    }

    public void freeBlockValue(Integer value) {
        available.add(value);
    }

    public int blocksAvailable() {
        return available.size();
    }

}
