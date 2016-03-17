package cells.iterator.range;

import cells.CellRange;
import cells.iterator.IterationStrategyIndexResolver;

/**
 * @author Dmitriy Tseyler
 */
public class RangeIterator extends AbstractRangeIterator {

    public RangeIterator(CellRange range) {
        this(range, RangeIterationStrategy.ROW);
    }

    public RangeIterator(CellRange range, RangeIterationStrategy strategy) {
        this(range, strategy, 1);
    }

    public RangeIterator(CellRange range, RangeIterationStrategy strategy, int offset) {
        super(range, strategy, offset);
        setBegin(strategy.getResolver().getFirstRange(range));
    }

    @Override
    boolean needChange(CellRange range, IterationStrategyIndexResolver resolver) {
        return resolver.getFirst(range) < resolver.getLast(this.range);
    }

    @Override
    public CellRange getBegin() {
        return null;
    }
}
