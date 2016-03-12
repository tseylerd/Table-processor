package ui.laf.span.managers;

import cells.pointer.CellPointer;
import cells.CellRange;
import ui.laf.span.spanner.Spanner;

/**
 * @author Dmitriy Tseyler
 */
public class UpSpanManager extends SpanManager {

    public UpSpanManager() {
        super(Spanner.UP);
    }

    protected boolean shouldAdd(CellPointer newPointer) {
        CellRange last = getLast();
        return newPointer.getRow() < last.getFirstRow();
    }

    protected boolean needRemove(CellPointer newPointer) {
        CellRange last = getLast();
        return last != null && last.getLastRow() < newPointer.getRow();
    }

    @Override
    protected CellRange createNextRange() {
        CellRange range = getLast();
        CellPointer start = CellPointer.getPointer(range.getFirstRow() - 1, getFirstColumn());
        CellPointer end = CellPointer.getPointer(range.getFirstRow() - 1, getLastColumn());
        return new CellRange(start, end);
    }
}
