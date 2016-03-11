package cells.iterator;

import cells.CellRange;

import java.util.Iterator;

/**
 * @author Dmitriy Tseyler
 */
public abstract class AbstractSpreadSheetIterator<T> implements Iterator<T> {
    protected final CellRange range;
    private final IterationStrategy<T, AbstractSpreadSheetIterator> strategy;
    private final int offset;
    private T current;

    public AbstractSpreadSheetIterator(CellRange range, IterationStrategy<T, AbstractSpreadSheetIterator> strategy, int offset) {
        this.range = range;
        this.strategy = strategy;
        this.offset = offset;
        current = getBegin();
    }

    @Override
    public T next() {
        T result = current;
        current = nextOf(current);
        return result;
    }

    protected T nextOf(T value) {
        return strategy.nextOf(value, this);
    }

    public abstract T getBegin();

    public int getOffset() {
        return offset;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    protected void setBegin(T value) {
        current = value;
    }
}
