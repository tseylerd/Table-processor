package ui.laf.span.managers;

import cells.pointer.CellPointer;
import cells.CellRange;
import ui.laf.span.spanner.Spanner;

/**
 * Spanning left
 * @author Dmitriy Tseyler
 */
public class LeftSpanManager extends SpanManager {

    public LeftSpanManager() {
        super(Spanner.LEFT);
    }

    @Override
    protected boolean shouldAdd(CellPointer newPointer) {
        CellRange last = getLast();
        return newPointer.getColumn() < last.getFirstColumn();
    }

    @Override
    protected boolean needRemove(CellPointer newPointer) {
        CellRange last = getLast();
        return last != null && last.getLastColumn() < newPointer.getColumn();
    }

    @Override
    protected CellRange createNextRange() {
        CellRange range = getLast();
        CellPointer start = CellPointer.getPointer(getFirstRow(), range.getFirstColumn() - 1);
        CellPointer end = CellPointer.getPointer(getLastRow(), range.getFirstColumn() - 1);
        return new CellRange(start, end);
    }
}
