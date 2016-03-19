package cells.iterator;

import cells.CellRange;

import java.util.Iterator;

/**
 * Base class of all spread sheet iterators
 * @see cells.iterator.cell.AbstractCellIterator
 * @see cells.iterator.range.AbstractRangeIterator
 * @author Dmitriy Tseyler
 */
public abstract class AbstractSpreadSheetIterator<T, U extends IterationStrategy<T, ? extends AbstractSpreadSheetIterator>> implements Iterator<T> {
    protected final CellRange range;
    protected final U strategy;
    private final int offset;
    private T current;

    protected AbstractSpreadSheetIterator(CellRange range, U strategy, int offset) {
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

    public abstract T getBegin();
    protected abstract T nextOf(T value);
}
