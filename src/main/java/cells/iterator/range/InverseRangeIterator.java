package cells.iterator.range;

import cells.CellRange;
import cells.iterator.IterationStrategyIndexResolver;

/**
 * Iterator over {@link CellRange} rows or columns.
 * This is the reverse order iterator. It begins in the right/lower column/row.
 * @see RangeIterator
 * @author Dmitriy Tseyler
 */
public class InverseRangeIterator extends AbstractRangeIterator {

    public InverseRangeIterator(CellRange range) {
        this(range, RangeIterationStrategy.ROW);
    }

    public InverseRangeIterator(CellRange range, RangeIterationStrategy strategy) {
        super(range, strategy, -1);
    }

    @Override
    boolean needChange(CellRange range, IterationStrategyIndexResolver resolver) {
        return resolver.getFirst(range) > resolver.getFirst(this.range);
    }

    @Override
    public CellRange getBegin() {
        return strategy.getResolver().getLastRange(range);
    }
}
