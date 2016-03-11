package cells.iterator;

import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public interface IterationStrategyCompareResolver {
    int getFirst(CellRange range);
    int getLast(CellRange range);
    CellRange getFirstRange(CellRange range);
    CellRange getLastRange(CellRange range);
}
