package homemade.game.state.impl;

import java.util.LinkedList;
import java.util.Random;

public class BlockValuePool {
    private LinkedList<Integer> available;
    private Random random; // TODO : tests, fixed random, you know the drill

    public BlockValuePool(int max, int total) {
        if (total % max != 0) {
            throw new IllegalArgumentException("block value pool is supposed to have equal amount of all values");
        }

        available = new LinkedList<>();

        for (int i = 0; i < total; i++) {
            available.add(i % max + 1);
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
