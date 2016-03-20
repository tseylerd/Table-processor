package cells.iterator.cell;

import cells.pointer.CellPointer;
import cells.CellRange;

/**
 * Iterator over {@link CellRange} cell by cell.
 * This is the reverse order iterator. It begins in the right lower corner.
 * @see CellIterator
 * @author Dmitriy Tseyler
 */
public class InverseCellIterator extends AbstractCellIterator {

    public InverseCellIterator(CellRange range) {
        this(range, CellIterationStrategy.COLUMN_CHANGE_FIRST);
    }

    public InverseCellIterator(CellRange range, CellIterationStrategy strategy) {
        super(range, strategy, -1);
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
    public CellPointer getBegin() {
        return range.getEnd();
    }
}
