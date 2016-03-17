package cells.iterator.range;

import cells.CellRange;
import cells.iterator.AbstractSpreadSheetIterator;
import cells.iterator.IterationStrategy;
import cells.iterator.IterationStrategyIndexResolver;

/**
 * @author Dmitriy Tseyler
 */
public abstract class AbstractRangeIterator extends AbstractSpreadSheetIterator<CellRange> {
    public AbstractRangeIterator(CellRange range, IterationStrategy strategy, int offset) {
        //noinspection unchecked
        super(range, strategy, offset);
    }

    abstract boolean needChange(CellRange range, IterationStrategyIndexResolver resolver);
}
