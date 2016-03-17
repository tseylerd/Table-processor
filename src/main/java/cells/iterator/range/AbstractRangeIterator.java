package cells.iterator.range;

import cells.CellRange;
import cells.iterator.AbstractSpreadSheetIterator;
import cells.iterator.IterationStrategy;
import cells.iterator.IterationStrategyIndexResolver;

/**
 * @author Dmitriy Tseyler
 */
public abstract class AbstractRangeIterator extends AbstractSpreadSheetIterator<CellRange, RangeIterationStrategy> {
    public AbstractRangeIterator(CellRange range, RangeIterationStrategy strategy, int offset) {
        super(range, strategy, offset);
    }

    abstract boolean needChange(CellRange range, IterationStrategyIndexResolver resolver);

    @Override
    protected CellRange nextOf(CellRange value) {
        return strategy.nextOf(value, this);
    }
}
