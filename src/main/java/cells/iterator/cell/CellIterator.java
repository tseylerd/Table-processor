package cells.iterator.cell;

import cells.CellPointer;
import cells.CellRange;

/**
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
