package ui.laf.span.mode;

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
    protected int firstColumn;
    protected int firstRow;
    protected int lastColumn;
    protected int lastRow;

    public SpanManager() {
        ranges = new LinkedList<>();
    }

    public void newCell(CellPointer pointer) {
        while (needAdd(ranges.peekLast(), pointer)) {
            CellRange last = ranges.peekLast();
            if (last == null) {
                last = new CellRange(firstRow, firstColumn, lastRow, lastColumn);
            }
            ranges.addLast(getLast(last));
        }
        while (needRemove(ranges.peekLast(), pointer)){
            ranges.removeLast();
        }
        log();
    }

    protected abstract boolean needAdd(CellRange last, CellPointer newPointer);
    protected abstract boolean needRemove(CellRange last, CellPointer newPointer);
    protected abstract CellRange getLast(CellRange pointer);

    public void setStartRange(CellRange range) {
        firstColumn = range.getFirstColumn();
        lastColumn = range.getLastColumn();
        firstRow = range.getFirstRow();
        lastRow = range.getLastRow();
    }

    public void span(SpreadSheetModel model) {

    }

    public boolean isInside(CellPointer pointer) {
        for (CellRange range : ranges) {
            if (range.isInside(pointer))
                return true;
        }
        return false;
    }

    private void log() {
        for (CellRange range : ranges) {
            logger.log(Level.INFO, getClass().getName() + ": " + range.toString());
        }
    }

    public static SpanManager getMode(boolean leftSide, boolean rightSide, boolean upSide, boolean downSide) {
        if (leftSide) {
            return new LeftSpanManager();
        } else if (rightSide) {
            return new RightSpanManager();
        } else if (upSide) {
            return new UpSpanManager();
        } else if (downSide) {
            return new DownSpanManager();
        }
        return null;
    }
}
