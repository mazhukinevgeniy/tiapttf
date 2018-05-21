package homemade.utils;

import java.util.ArrayList;
import java.util.List;

public class PiecewiseConstantFunction<Separator extends Comparable<Separator>, Value> {
    private List<Value> values;
    private List<Separator> separators;


    public PiecewiseConstantFunction(List<Separator> separators, List<Value> values) {
        if (separators.size() + 1 != values.size()) {
            throw new RuntimeException("incorrect data is passed to PiecewiseConstantFunction");
        } else {
            this.separators = new ArrayList<Separator>(separators);
            this.values = new ArrayList<Value>(values);
        }
    }

    public Value getValueAt(Separator pointOfInterval) {
        int interval = 0;
        int maxInterval = separators.size();

        while (interval < maxInterval && pointOfInterval.compareTo(separators.get(interval)) > 0) {
            interval++;
        }

        return values.get(interval);
    }
}
