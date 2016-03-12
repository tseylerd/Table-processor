package cells.iterator.cell;

import cells.pointer.CellPointer;
import cells.CellRange;
import cells.iterator.IterationStrategy;

/**
 * @author Dmitriy Tseyler
 */
public class InverseCellIterator extends AbstractCellIterator {

    public InverseCellIterator(CellRange range) {
        this(range, CellIterationStrategy.COLUMN_ROW);
    }

    public InverseCellIterator(CellRange range, IterationStrategy strategy) {
        this(range, strategy, -1);
    }

    public InverseCellIterator(CellRange range, IterationStrategy strategy, int offset) {
        super(range, strategy, offset);
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
