package cells.iterator;

import cells.CellRange;

/**
 * Helps to {@link cells.iterator.range.AbstractRangeIterator} to resolve what column is first and last in range
 * @author Dmitriy Tseyler
 */
public class ColumnResolver implements IterationStrategyIndexResolver {
    @Override
    public int getFirst(CellRange range) {
        return range.getFirstColumn();
    }

    @Override
    public int getLast(CellRange range) {
        return range.getLastColumn();
    }

    @Override
    public CellRange getFirstRange(CellRange range) {
        return range.getFirstColumnRange();
    }

    @Override
    public CellRange getLastRange(CellRange range) {
        return range.getLastColumnRange();
    }
}
