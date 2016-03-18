package cells.iterator;

import cells.CellRange;

/**
 * Helps range iterator to resolve what row is first and last in range
 * @author Dmitriy Tseyler
 */
public class RowResolver implements IterationStrategyIndexResolver {
    @Override
    public int getFirst(CellRange range) {
        return range.getFirstRow();
    }

    @Override
    public int getLast(CellRange range) {
        return range.getLastRow();
    }

    @Override
    public CellRange getFirstRange(CellRange range) {
        return range.getFirstRowRange();
    }

    @Override
    public CellRange getLastRange(CellRange range) {
        return range.getLastRowRange();
    }
}
