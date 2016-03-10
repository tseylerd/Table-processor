package ui.laf.span.mode;

import cells.CellPointer;
import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public class LeftSpanManager extends SpanManager {

    @Override
    protected boolean needAdd(CellRange last, CellPointer newPointer) {
        return (last == null && newPointer.getColumn() <= firstColumn) || (last != null && newPointer.getColumn() < last.getFirstColumn());
    }

    @Override
    protected boolean needRemove(CellRange last, CellPointer newPointer) {
        return last != null && last.getLastColumn() < newPointer.getColumn();
    }

    @Override
    protected CellRange getLast(CellRange range) {
        CellPointer start = new CellPointer(firstRow, range.getFirstColumn() - 1);
        CellPointer end = new CellPointer(lastRow, range.getFirstColumn() - 1);
        return new CellRange(start, end);
    }
}
