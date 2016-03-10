package ui.laf.span.mode;

import cells.CellPointer;
import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public class UpSpanManager extends SpanManager {

    protected boolean needAdd(CellRange last, CellPointer newPointer) {
        return (last == null && newPointer.getRow() <= firstRow) || (last != null && newPointer.getRow() < last.getFirstRow());
    }

    protected boolean needRemove(CellRange last, CellPointer newPointer) {
        return last != null && last.getLastRow() < newPointer.getRow();
    }

    @Override
    protected CellRange getLast(CellRange range) {
        CellPointer start = new CellPointer(range.getFirstRow() - 1, firstColumn);
        CellPointer end = new CellPointer(range.getFirstRow() - 1, lastColumn);
        return new CellRange(start, end);
    }
}
