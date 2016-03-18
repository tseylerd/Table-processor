package cells.iterator.cell;

import cells.pointer.CellPointer;
import cells.CellRange;

/**
 * Iterator over {@link CellRange} cell by cell.
 * This is the normal order iterator. It begins in the left upper corner.
 * @see InverseCellIterator
 * @author Dmitriy Tseyler
 */
public class CellIterator extends AbstractCellIterator {

    public CellIterator(CellRange range) {
        super(range, 1);
    }

    public CellIterator(CellRange range, CellIterationStrategy strategy) {
        super(range, strategy, 1);
    }

    @Override
    boolean needChangeColumn(int column) {
        return column < range.getEnd().getColumn();
    }

    @Override
    boolean needChangeRow(int row) {
        return row < range.getEnd().getRow();
    }

    @Override
    public CellPointer getBegin() {
        return range.getBegin();
    }
}
