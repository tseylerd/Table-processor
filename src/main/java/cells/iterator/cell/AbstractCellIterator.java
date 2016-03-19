package cells.iterator.cell;

import cells.pointer.CellPointer;
import cells.CellRange;
import cells.iterator.AbstractSpreadSheetIterator;


/**
 * Abstract cell iterator. Used for iteration over the {@link CellRange}.
 * @see CellIterationStrategy
 * @author Dmitriy Tseyler
 */
public abstract class AbstractCellIterator extends AbstractSpreadSheetIterator<CellPointer, CellIterationStrategy> {
    AbstractCellIterator(CellRange range, int offset) {
        this(range, CellIterationStrategy.COLUMN_CHANGE_FIRST, offset);
    }

    AbstractCellIterator(CellRange range, CellIterationStrategy strategy, int offset) {
        super(range, strategy, offset);
    }

    abstract boolean needChangeColumn(int column);
    abstract boolean needChangeRow(int row);

    @Override
    protected CellPointer nextOf(CellPointer value) {
        return strategy.nextOf(value, this);
    }
}
