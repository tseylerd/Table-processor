package cells.iterator.cell;

import cells.pointer.CellPointer;
import cells.CellRange;
import cells.iterator.AbstractSpreadSheetIterator;
import cells.iterator.IterationStrategy;


/**
 * @author Dmitriy Tseyler
 */
public abstract class AbstractCellIterator extends AbstractSpreadSheetIterator<CellPointer, CellIterationStrategy> {
    protected AbstractCellIterator(CellRange range, int offset) {
        this(range, CellIterationStrategy.COLUMN_ROW, offset);
    }

    protected AbstractCellIterator(CellRange range, CellIterationStrategy strategy, int offset) {
        super(range, strategy, offset);
    }

    abstract boolean needChangeColumn(int column);
    abstract boolean needChangeRow(int row);

    @Override
    protected CellPointer nextOf(CellPointer value) {
        return strategy.nextOf(value, this);
    }
}
