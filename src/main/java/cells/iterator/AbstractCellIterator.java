package cells.iterator;

import cells.CellPointer;
import cells.CellRange;

import java.util.Iterator;

/**
 * @author Dmitriy Tseyler
 */
public abstract class AbstractCellIterator implements Iterator<CellPointer> {
    protected final CellRange range;
    private CellPointer current;
    private IterationStrategy strategy;

    protected AbstractCellIterator(CellRange range) {
        this(range, IterationStrategy.COLUMN_ROW);
    }

    protected AbstractCellIterator(CellRange range, IterationStrategy strategy) {
        this.strategy = strategy;
        this.range = range;
        current = getBegin();
    }

    abstract CellPointer getBegin();
    abstract boolean needChangeColumn(int column);
    abstract boolean needChangeRow(int row);
    abstract int getOffset();

    @Override
    public CellPointer next() {
        CellPointer result = current;
        current = nextOf(current);
        return result;
    }

    private CellPointer nextOf(CellPointer previous) {
        return strategy.nextOf(previous, this);
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }
}
