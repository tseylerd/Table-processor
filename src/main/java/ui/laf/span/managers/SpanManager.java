package ui.laf.span.managers;

import cells.pointer.CellPointer;
import cells.CellRange;
import ui.laf.span.spanner.Spanner;
import ui.table.SpreadSheetModel;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author dtseyler
 */
public abstract class SpanManager {
    private final LinkedList<CellRange> ranges;
    private final Spanner spanner;

    private CellRange startRange;

    public SpanManager(Spanner spanner) {
        this.spanner = spanner;
        ranges = new LinkedList<>();
    }

    public void newCell(CellPointer pointer) {
        while (shouldAdd(pointer)) {
            ranges.addLast(createNextRange());
        }
        while (!ranges.isEmpty() && needRemove(pointer)){
            ranges.removeLast();
        }
    }

    protected CellRange getLast() {
        return ranges.peekLast() == null ? startRange : ranges.peekLast();
    }

    public void setStartRange(CellRange startRange) {
        this.startRange = startRange;
    }

    protected int getFirstColumn() {
        return startRange.getFirstColumn();
    }

    protected int getLastColumn() {
        return startRange.getLastColumn();
    }

    protected int getFirstRow() {
        return startRange.getFirstRow();
    }

    protected int getLastRow() {
        return startRange.getLastRow();
    }

    public boolean isInside(CellPointer pointer) {
        for (CellRange range : ranges) {
            if (range.isInside(pointer))
                return true;
        }
        return startRange.isInside(pointer);
    }

    protected abstract boolean shouldAdd(CellPointer newPointer);

    protected abstract boolean needRemove(CellPointer newPointer);

    protected abstract CellRange createNextRange();

    public void span(SpreadSheetModel model) {
        if (ranges.isEmpty())
            return;

        int minRow = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int minColumn = Integer.MAX_VALUE;
        int maxColumn = Integer.MIN_VALUE;
        for (CellRange range : ranges) {
            if (range.getFirstRow() < minRow) {
                minRow = range.getFirstRow();
            }
            if (range.getLastRow() > maxRow) {
                maxRow = range.getLastRow();
            }
            if (range.getFirstColumn() < minColumn) {
                minColumn = range.getFirstColumn();
            }
            if (range.getLastColumn() > maxColumn) {
                maxColumn = range.getLastColumn();
            }
        }
        CellRange endRange = new CellRange(minRow, minColumn, maxRow, maxColumn);
        spanner.span(model, startRange, endRange);
    }
}
