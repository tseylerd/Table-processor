package cells.iterator.range;

import cells.CellRange;
import cells.iterator.IterationStrategyCompareResolver;

/**
 * @author Dmitriy Tseyler
 */
public class InverseRangeIterator extends AbstractRangeIterator {

    public InverseRangeIterator(CellRange range) {
        this(range, RangeIterationStrategy.ROW);
    }

    public InverseRangeIterator(CellRange range, RangeIterationStrategy strategy) {
        this(range, strategy, -1);
    }

    public InverseRangeIterator(CellRange range, RangeIterationStrategy strategy, int offset) {
        super(range, strategy, offset);
        setBegin(strategy.getResolver().getLastRange(range));
    }

    @Override
    boolean needChange(CellRange range, IterationStrategyCompareResolver resolver) {
        return resolver.getFirst(range) > resolver.getFirst(this.range);
    }

    @Override
    public CellRange getBegin() {
        return null;
    }
}
