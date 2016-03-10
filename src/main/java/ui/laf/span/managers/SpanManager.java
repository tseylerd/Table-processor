package ui.laf.span.managers;

import cells.CellPointer;
import cells.CellRange;
import ui.table.SpreadSheetModel;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author dtseyler
 */
public abstract class SpanManager {
    private static final Logger logger = Logger.getLogger("SpanManager");

    private final LinkedList<CellRange> ranges;
    private CellRange startRange;

    public SpanManager() {
        ranges = new LinkedList<>();
    }

    public void newCell(CellPointer pointer) {
        CellRange last = ranges.peekLast() == null ? startRange : ranges.peekLast();
        while (shouldAdd(pointer)) {
            ranges.addLast(createNextRange());
        }
        while (needRemove(pointer)){
            ranges.removeLast();
        }
        log();
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

    private void log() {
        for (CellRange range : ranges) {
            logger.log(Level.INFO, getClass().getName() + ": " + range.toString());
        }
    }

    protected abstract boolean shouldAdd(CellPointer newPointer);

    protected abstract boolean needRemove(CellPointer newPointer);

    protected abstract CellRange createNextRange();

    public void move(SpreadSheetModel model) {

    }
}
