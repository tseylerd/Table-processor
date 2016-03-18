package cells.iterator;

import cells.CellRange;

/**
 * Base interface for using in range iterators to determine, which row/column is first and last
 * @author Dmitriy Tseyler
 */
public interface IterationStrategyIndexResolver {
    int getFirst(CellRange range);
    int getLast(CellRange range);
    CellRange getFirstRange(CellRange range);
    CellRange getLastRange(CellRange range);
}
