package cells.iterator;

import cells.CellPointer;
import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public class InverseCellIterator extends AbstractCellIterator {

    InverseCellIterator(CellRange range) {
        super(range);
    }

    @Override
    protected int getOffset() {
        return -1;
    }

    @Override
    protected boolean needChangeColumn(int column) {
        return column > range.getBegin().getColumn();
    }

    @Override
    protected boolean needChangeRow(int row) {
        return row > range.getBegin().getRow();
    }

    @Override
    protected CellPointer getBegin() {
        return range.getEnd();
    }
}
