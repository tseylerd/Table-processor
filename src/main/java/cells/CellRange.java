package cells;

import cells.iterator.cell.CellIterator;
import cells.iterator.cell.InverseCellIterator;
import cells.iterator.cell.CellIterationStrategy;
import cells.iterator.range.InverseRangeIterator;
import cells.iterator.range.RangeIterationStrategy;
import cells.iterator.range.RangeIterator;
import cells.pointer.CellPointer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.*;

/**
 * @author Dmitriy Tseyler
 */
public class CellRange implements Iterable<CellPointer>, Transferable {
    public static final DataFlavor CELL_RANGE_DATA_FLAVOUR = new DataFlavor(CellRange.class, "Range");

    private final CellPointer begin;
    private final CellPointer end;

    public CellRange(int startRow, int startColumn, int endRow, int endColumn) {
        this(CellPointer.getPointer(startRow, startColumn), CellPointer.getPointer(endRow, endColumn));
    }

    public CellRange(CellPointer begin, CellPointer end) {
        this.begin = begin;
        this.end = end;
    }

    public CellPointer getBegin() {
        return begin;
    }

    public CellPointer getEnd() {
        return end;
    }

    public boolean isInside(CellPointer pointer) {
        return  pointer != null &&
                begin.getColumn() <= pointer.getColumn() &&
                begin.getRow() <= pointer.getRow() &&
                end.getRow() >= pointer.getRow() &&
                end.getColumn() >= pointer.getColumn();
    }

    public boolean isInside(CellRange range) {
        return  range != null &&
                getFirstColumn() <= range.getFirstColumn() &&
                getFirstRow() <= range.getFirstRow() &&
                getLastColumn() >= range.getLastColumn() &&
                getLastRow() >= range.getLastRow();
    }

    public int getFirstColumn() {
        return begin.getColumn();
    }

    public int getLastColumn() {
        return end.getColumn();
    }

    public int getFirstRow() {
        return begin.getRow();
    }

    public int getLastRow() {
        return end.getRow();
    }

    public CellRange getFirstRowRange() {
        return new CellRange(getFirstRow(), getFirstColumn(), getFirstRow(), getLastColumn());
    }

    public CellRange getFirstColumnRange() {
        return new CellRange(getFirstRow(), getFirstColumn(), getLastRow(), getFirstColumn());
    }

    public CellRange getLastColumnRange() {
        return new CellRange(getFirstRow(), getLastColumn(), getLastRow(), getLastColumn());
    }

    public CellRange getLastRowRange() {
        return new CellRange(getLastRow(), getFirstColumn(), getLastRow(), getLastColumn());
    }

    public static CellRange createCellRange(int[] rows, int[] columns) {
        if (rows.length == 0 || columns.length == 0) {
            return null;
        }

        CellPointer start = CellPointer.getPointer(rows[0], columns[0]);
        CellPointer end = CellPointer.getPointer(rows[rows.length - 1], columns[columns.length - 1]);
        return new CellRange(start, end);
    }

    public Iterator<CellPointer> inverseColumnRowIterator() {
        return new InverseCellIterator(this);
    }

    public Iterator<CellPointer> inverseRowColumnIterator() {
        return new InverseCellIterator(this, CellIterationStrategy.ROW_COLUMN);
    }

    public Iterator<CellPointer> rowColumnIterator() {
        return new CellIterator(this, CellIterationStrategy.ROW_COLUMN);
    }

    public Iterator<CellRange> rangeIterator() {
        return new RangeIterator(this);
    }

    public Iterator<CellRange> columnRangeIterator() {
        return new RangeIterator(this, RangeIterationStrategy.COLUMN);
    }

    public Iterator<CellRange> inverseRangeIterator() {
        return new InverseRangeIterator(this);
    }

    public Iterator<CellRange> inverseColumnRangeIterator() {
        return new InverseRangeIterator(this, RangeIterationStrategy.COLUMN);
    }

    public int size() {
        return (end.getColumn() - begin.getColumn() + 1) * (end.getRow() - begin.getRow() + 1);
    }

    public List<CellRange> split(CellRange range) {
        CellPointer rangeBegin = range.getBegin();
        CellPointer rangeEnd = range.getEnd();
        int xMax = Math.min(rangeEnd.getColumn(), end.getColumn());
        int yMax = Math.min(rangeEnd.getRow(), end.getRow());
        int xMin = Math.max(rangeBegin.getColumn(), begin.getColumn());
        int yMin = Math.max(rangeBegin.getRow(), begin.getRow());

        int xOverlap = Math.max(0, xMax - xMin);
        int yOverlap = Math.max(0, yMax - yMin);
        if (xOverlap == 0 || yOverlap == 0) {
            return null;
        }

        List<CellRange> ranges = new ArrayList<>();
        CellRange upper = getUpperRange(xMin, yMin, xMax, yMax);
        CellRange bottom = getBottomRange(xMin, yMin, xMax, yMax);
        CellRange lefter = getLefterRange(xMin, yMin, xMax, yMax);
        CellRange righter = getRighterRange(xMin, yMin, xMax, yMax);
        lefter = concatenate(lefter, upper, bottom);
        righter = concatenate(righter, upper, bottom);
        addIfNotNull(ranges, upper);
        addIfNotNull(ranges, bottom);
        addIfNotNull(ranges, lefter);
        addIfNotNull(ranges, righter);
        return Collections.unmodifiableList(ranges);
    }

    public SplittedRange splitHonestly(CellRange range) {
        CellPointer rangeBegin = range.getBegin();
        CellPointer rangeEnd = range.getEnd();
        int xMax = Math.min(rangeEnd.getColumn(), end.getColumn());
        int yMax = Math.min(rangeEnd.getRow(), end.getRow());
        int xMin = Math.max(rangeBegin.getColumn(), begin.getColumn());
        int yMin = Math.max(rangeBegin.getRow(), begin.getRow());

        int xOverlap = Math.max(0, xMax - xMin);
        int yOverlap = Math.max(0, yMax - yMin);
        if (xOverlap == 0 || yOverlap == 0) {
            return null;
        }

        CellRange upper = getUpperRange(xMin, yMin, xMax, yMax);
        CellRange bottom = getBottomRange(xMin, yMin, xMax, yMax);
        CellRange lefter = getLefterRange(xMin, yMin, xMax, yMax);
        CellRange righter = getRighterRange(xMin, yMin, xMax, yMax);
        return new SplittedRange(upper, bottom, lefter, righter, new CellRange(yMin, xMin, yMax, xMax));
    }

    private CellRange concatenate(CellRange side, CellRange upper, CellRange bottom) {
        if (side == null) {
            return side;
        }
        if (upper != null) {
            side = new CellRange(upper.getBegin().getRow(), side.getBegin().getColumn(), side.getEnd().getRow(), side.getEnd().getColumn());
        }
        if (bottom != null) {
            side = new CellRange(side.getBegin().getRow(), side.getBegin().getColumn(), bottom.getEnd().getRow(), side.getEnd().getColumn());
        }
        return side;
    }

    private void addIfNotNull(List<CellRange> ranges, CellRange range) {
        if (range != null) {
            ranges.add(range);
        }
    }

    private CellRange getUpperRange(int xMin, int yMin, int xMax, int yMax) {
        if (yMin <= begin.getRow()) {
            return null;
        }

        int xStart = begin.getColumn();
        int yStart = begin.getRow();
        int xEnd = end.getColumn();
        return new CellRange(yStart, xStart, yMin - 1, xEnd);
    }

    private CellRange getBottomRange(int xMin, int yMin, int xMax, int yMax) {
        if (yMax >= end.getRow()) {
            return null;
        }
        return new CellRange(yMax + 1, begin.getColumn(), end.getRow(), end.getColumn());
    }

    private CellRange getLefterRange(int xMin, int yMin, int xMax, int yMax) {
        if (xMin <= begin.getColumn()) {
            return null;
        }
        return new CellRange(Math.max(yMin, begin.getRow()), begin.getColumn(), Math.min(yMax, end.getRow()), xMin - 1);
    }

    private CellRange getRighterRange(int xMin, int yMin, int xMax, int yMax) {
        if (xMax >= end.getColumn()) {
            return null;
        }
        return new CellRange(Math.max(yMin, begin.getRow()), xMax + 1, Math.min(yMax, end.getRow()), end.getColumn());
    }

    @Override
    public String toString() {
        return begin.toString() + ":" + end.toString();
    }

    /* ================== Iterable ================== */

    @Override
    public Iterator<CellPointer> iterator() {
        return new CellIterator(this);
    }

    /* ================== Transferable ================== */

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {CELL_RANGE_DATA_FLAVOUR};
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;
    }

    @Override
    public int hashCode() {
        return begin.hashCode() + end.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CellRange)) {
            return false;
        }
        CellRange range = (CellRange)obj;
        return range.begin.equals(begin) && range.end.equals(end);
    }
}
