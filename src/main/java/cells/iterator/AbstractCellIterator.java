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

    protected AbstractCellIterator(CellRange range) {
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
