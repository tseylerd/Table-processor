package ui.laf.span.managers;

import cells.pointer.CellPointer;
import cells.CellRange;
import ui.laf.span.spanner.Spanner;

/**
 * @author Dmitriy Tseyler
 */
public class DownSpanManager extends SpanManager {

    public DownSpanManager() {
        super(Spanner.DOWN);
    }

    protected boolean shouldAdd(CellPointer newPointer) {
        CellRange last = getLast();
        return newPointer.getRow() > last.getLastRow();
    }

    protected boolean needRemove(CellPointer newPointer) {
        CellRange last = getLast();
        return last != null && last.getLastRow() > newPointer.getRow();
    }

    @Override
    protected CellRange createNextRange() {
        CellRange range = getLast();
        CellPointer start = CellPointer.getPointer(range.getLastRow() + 1, getFirstColumn());
        CellPointer end = CellPointer.getPointer(range.getLastRow() + 1, getLastColumn());
        return new CellRange(start, end);
    }
}
