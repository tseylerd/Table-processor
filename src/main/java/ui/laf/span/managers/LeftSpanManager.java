package ui.laf.span.managers;

import cells.CellPointer;
import cells.CellRange;
import ui.laf.span.spanner.Spanner;

/**
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
        CellPointer start = new CellPointer(getFirstRow(), range.getFirstColumn() - 1);
        CellPointer end = new CellPointer(getLastRow(), range.getFirstColumn() - 1);
        return new CellRange(start, end);
    }
}
