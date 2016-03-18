package ui.laf.span.managers;

import cells.pointer.CellPointer;
import cells.CellRange;
import ui.laf.span.spanner.Spanner;

/**
 * Spanning right
 * @author Dmitriy Tseyler
 */
public class RightSpanManager extends SpanManager {

    public RightSpanManager() {
        super(Spanner.RIGHT);
    }

    @Override
    protected boolean shouldAdd(CellPointer newPointer) {
        CellRange last = getLast();
        return newPointer.getColumn() > last.getLastColumn();
    }

    @Override
    protected boolean needRemove(CellPointer newPointer) {
        CellRange last = getLast();
        return last != null && last.getLastColumn() > newPointer.getColumn();
    }

    @Override
    protected CellRange createNextRange() {
        CellRange range = getLast();
        CellPointer start = CellPointer.getPointer(getFirstRow(), range.getLastColumn() + 1);
        CellPointer end = CellPointer.getPointer(getLastRow(), range.getLastColumn() + 1);
        return new CellRange(start, end);
    }
}
