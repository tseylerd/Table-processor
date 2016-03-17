package cells.iterator.range;

import cells.CellRange;
import cells.iterator.ColumnResolver;
import cells.iterator.IterationStrategy;
import cells.iterator.IterationStrategyIndexResolver;
import cells.iterator.RowResolver;

/**
 * @author Dmitriy Tseyler
 */
public enum RangeIterationStrategy implements IterationStrategy<CellRange, AbstractRangeIterator> {
    ROW(new RowResolver()) {
        @Override
        CellRange next(CellRange range, AbstractRangeIterator iterator) {
            int rowStart = range.getFirstRow() + iterator.getOffset();
            int columnStart = range.getFirstColumn();
            int rowEnd = range.getFirstRow() + iterator.getOffset();
            int columnEnd = range.getLastColumn();
            return new CellRange(rowStart, columnStart, rowEnd, columnEnd);
        }
    },
    COLUMN(new ColumnResolver()) {
        @Override
        CellRange next(CellRange range, AbstractRangeIterator iterator) {
            int rowStart = range.getFirstRow();
            int columnStart = range.getFirstColumn() + iterator.getOffset();
            int rowEnd = range.getLastRow();
            int columnEnd = range.getLastColumn() + iterator.getOffset();
            return new CellRange(rowStart, columnStart, rowEnd, columnEnd);
        }
    };

    private final IterationStrategyIndexResolver resolver;

    RangeIterationStrategy(IterationStrategyIndexResolver resolver)
    {
        this.resolver = resolver;
    }

    public CellRange nextOf(CellRange range, AbstractRangeIterator iterator){
        CellRange next = null;
        if (iterator.needChange(range, resolver)) {
            next = next(range, iterator);
        }
        return next;
    }

    public IterationStrategyIndexResolver getResolver() {
        return resolver;
    }

    abstract CellRange next(CellRange range, AbstractRangeIterator iterator);
}
