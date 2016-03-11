package cells.iterator;

import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public class ColumnResolver implements IterationStrategyCompareResolver {
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
