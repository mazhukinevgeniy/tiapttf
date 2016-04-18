package homemade.game.model;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by user3 on 29.03.2016.
 */
public class NumberPool
{
    private LinkedList<Integer> usedNumbers, availableNumbers;
    private Random random;

    NumberPool(int max)
    {
        usedNumbers = new LinkedList<Integer>();
        availableNumbers = new LinkedList<Integer>();

        for (int i = 0; i < max; i++)
        {
            availableNumbers.add(i + 1);
        }

        random = new Random();
    }

    public int takeNumber()
    {
        int length = availableNumbers.size();
        int position = random.nextInt(length);
        int number = availableNumbers.get(position);

        usedNumbers.add(number);
        availableNumbers.remove(position);

        return number;
    }

    public void freeNumber(int value)
    {
        usedNumbers.remove(Integer.valueOf(value));
        //because we want to remove by object and not by its position in the list
        availableNumbers.add(value);
    }

    public int numbersAvailable()
    {
        return availableNumbers.size();
    }

    public boolean isEmpty()
    {
        return availableNumbers.isEmpty();
    }

}
