package cells.iterator;

import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public class RowResolver implements IterationStrategyCompareResolver {
    @Override
    public int getFirst(CellRange range) {
        return range.getFirstRow();
    }

    @Override
    public int getLast(CellRange range) {
        return range.getLastColumn();
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
