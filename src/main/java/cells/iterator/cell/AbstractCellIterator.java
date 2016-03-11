package cells.iterator.cell;

import cells.CellPointer;
import cells.CellRange;
import cells.iterator.AbstractSpreadSheetIterator;
import cells.iterator.IterationStrategy;


/**
 * @author Dmitriy Tseyler
 */
public abstract class AbstractCellIterator extends AbstractSpreadSheetIterator<CellPointer> {
    protected AbstractCellIterator(CellRange range, int offset) {
        this(range, CellIterationStrategy.COLUMN_ROW, offset);
    }

    protected AbstractCellIterator(CellRange range, IterationStrategy strategy, int offset) {
        super(range, strategy, offset);
    }

    abstract boolean needChangeColumn(int column);
    abstract boolean needChangeRow(int row);
}
