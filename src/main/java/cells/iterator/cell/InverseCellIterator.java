package cells.iterator.cell;

import cells.pointer.CellPointer;
import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public class InverseCellIterator extends AbstractCellIterator {

    public InverseCellIterator(CellRange range) {
        this(range, CellIterationStrategy.COLUMN_ROW);
    }

    public InverseCellIterator(CellRange range, CellIterationStrategy strategy) {
        this(range, strategy, -1);
    }

    public InverseCellIterator(CellRange range, CellIterationStrategy strategy, int offset) {
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
