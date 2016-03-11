package cells.iterator.range;

import cells.CellRange;
import cells.iterator.ColumnResolver;
import cells.iterator.IterationStrategy;
import cells.iterator.IterationStrategyCompareResolver;
import cells.iterator.RowResolver;

import java.util.function.BiFunction;

/**
 * @author Dmitriy Tseyler
 */
public enum RangeIterationStrategy implements IterationStrategy<CellRange, AbstractRangeIterator> {
    ROW((range, iterator) -> new CellRange(range.getFirstRow() + iterator.getOffset(), range.getFirstColumn(),
            range.getFirstRow() + iterator.getOffset(), range.getLastColumn()),
            new RowResolver()),
    COLUMN((range, iterator) -> new CellRange(range.getFirstRow(), range.getFirstColumn() + iterator.getOffset(),
            range.getLastRow(), range.getLastColumn() + iterator.getOffset()),
            new ColumnResolver());

    private final BiFunction<CellRange, AbstractRangeIterator, CellRange> biFunction;
    private final IterationStrategyCompareResolver resolver;

    RangeIterationStrategy(BiFunction<CellRange, AbstractRangeIterator, CellRange> biFunction,
                           IterationStrategyCompareResolver resolver) {
        this.biFunction = biFunction;
        this.resolver = resolver;
    }

    public CellRange nextOf(CellRange range, AbstractRangeIterator iterator){
        CellRange next = null;
        if (iterator.needChange(range, resolver)) {
            next = biFunction.apply(range, iterator);
        }
        return next;
    }

    public IterationStrategyCompareResolver getResolver() {
        return resolver;
    }
}
