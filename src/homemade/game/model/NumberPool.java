package homemade.game.model;

import java.util.LinkedList;

/**
 * Created by user3 on 29.03.2016.
 */
class NumberPool
{

    private LinkedList<Integer> usedNumbers, availableNumbers;

    NumberPool(int max)
    {
        usedNumbers = new LinkedList<Integer>();
        availableNumbers = new LinkedList<Integer>();

        for (int i = 0; i < max; i++)
        {
            availableNumbers.add(i + 1);
        }
    }

    int takeNumber()
    {
        int length = availableNumbers.size();
        int position = (int)(Math.random() * (double)length);
        int number = availableNumbers.get(position);

        usedNumbers.add(number);
        availableNumbers.remove(position);

        return number;
    }

    void freeNumber(int value)
    {
        usedNumbers.remove(new Integer(value));
        //because we want to remove by object and not by its position in the list
        availableNumbers.add(value);
    }

    int numbersAvailable()
    {
        return availableNumbers.size();
    }

    boolean isEmpty()
    {
        return availableNumbers.isEmpty();
    }

}
