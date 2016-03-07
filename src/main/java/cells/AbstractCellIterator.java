package cells;

import java.util.Iterator;

/**
 * @author Dmitriy Tseyler
 */
public abstract class AbstractCellIterator implements Iterator<CellPointer> {
    protected final CellRange range;
    private CellPointer current;

    protected AbstractCellIterator(CellRange range) {
        this.range = range;
        current = getBegin();
    }

    protected abstract CellPointer getBegin();
    protected abstract boolean needChangeColumn(int column);
    protected abstract boolean needChangeRow(int row);
    protected abstract int getOffset();

    @Override
    public CellPointer next() {
        CellPointer result = current;
        current = nextOf(current);
        return result;
    }

    private CellPointer nextOf(CellPointer previous) {
        int row = previous.getRow();
        int column = previous.getColumn();
        if (needChangeColumn(column)) {
            column += getOffset();
        } else if (needChangeRow(row)) {
            row += getOffset();
            column = getBegin().getColumn();
        } else {
            return null;
        }
        return new CellPointer(row, column);
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }
}
