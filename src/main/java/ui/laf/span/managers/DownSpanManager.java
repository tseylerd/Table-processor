package ui.laf.span.managers;

import cells.CellPointer;
import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public class DownSpanManager extends SpanManager {

    protected boolean shouldAdd(CellPointer newPointer) {
        CellRange last = getLast();
        return newPointer.getRow() > last.getFirstRow();
    }

    protected boolean needRemove(CellPointer newPointer) {
        CellRange last = getLast();
        return last != null && last.getLastRow() > newPointer.getRow();
    }

    @Override
    protected CellRange createNextRange() {
        CellRange range = getLast();
        CellPointer start = new CellPointer(range.getFirstRow() + 1, getFirstColumn());
        CellPointer end = new CellPointer(range.getFirstRow() + 1, getLastColumn());
        return new CellRange(start, end);
    }
}
