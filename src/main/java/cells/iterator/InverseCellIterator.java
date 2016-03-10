package cells.iterator;

import cells.CellPointer;
import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public class InverseCellIterator extends AbstractCellIterator {

    public InverseCellIterator(CellRange range) {
        super(range);
    }

    @Override
    int getOffset() {
        return -1;
    }

    @Override
    boolean needChangeColumn(int column) {
        return column > range.getBegin().getColumn();
    }

    @Override
    boolean needChangeRow(int row) {
        return row > range.getBegin().getRow();
    }

    @Override
    CellPointer getBegin() {
        return range.getEnd();
    }
}
