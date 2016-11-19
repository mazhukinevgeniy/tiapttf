package homemade.game.fieldstructure;

import java.util.Iterator;

/**
 * Use this class if you need iterators which do not provide remove() method.
 */
public class SafeIterator<E> implements Iterator<E> {
    E[] immutableArray;
    private int length;
    private int position;


    /**
     * @param immutableArray Immutability must be assured by client.
     *                       The advantage is the fact you can, nearly at no cost,
     *                       create multiple iterators for the same data.
     * @param <Type>         Type of array, plain and simple. This is how static generics work in Java.
     */
    public static <Type> Iterator<Type> getForImmutableArray(Type[] immutableArray) {
        return new SafeIterator<Type>(immutableArray);
    }


    private SafeIterator(E[] immutableArray) {
        this.immutableArray = immutableArray;

        length = immutableArray.length;
        position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < length;
    }

    @Override
    public E next() {
        assert position < length;

        E toReturn = immutableArray[position];
        position++;

        return toReturn;
    }

    @Override
    final public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
