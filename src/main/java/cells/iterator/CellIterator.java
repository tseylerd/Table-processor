package cells.iterator;

import cells.CellPointer;
import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public class CellIterator extends AbstractCellIterator {

    public CellIterator(CellRange range) {
        super(range);
    }

    @Override
    CellPointer getBegin() {
        return range.getBegin();
    }

    @Override
    int getOffset() {
        return 1;
    }

    @Override
    boolean needChangeColumn(int column) {
        return column < range.getEnd().getColumn();
    }

    @Override
    boolean needChangeRow(int row) {
        return row < range.getEnd().getRow();
    }
}
