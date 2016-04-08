package homemade.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user3 on 08.04.2016.
 */
public class PiecewiseConstantFunction<Separator extends Comparable<Separator>, Value>
{
    private List<Value> values;
    private List<Separator> separators;


    public PiecewiseConstantFunction(List<Separator> separators, List<Value> values)
    {
        if (separators.size() + 1 != values.size())
        {
            throw new Error("incorrect data is passed to PiecewiseConstantFunction");
            //TODO: might as well check for unsorted and/or equal separators
        }
        else
        {
            this.separators = new ArrayList<Separator>(separators);
            this.values = new ArrayList<Value>(values);
        }
    }

    public Value getValueAt(Separator pointOfInterval)
    {
        int interval = 0;
        int maxInterval = separators.size();

        while (interval < maxInterval && pointOfInterval.compareTo(separators.get(interval)) > 0)
        {
            interval++;
        }

        return values.get(interval);
    }
}
