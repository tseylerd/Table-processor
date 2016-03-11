package cells;

import cells.iterator.cell.CellIterator;
import cells.iterator.cell.InverseCellIterator;
import cells.iterator.cell.CellIterationStrategy;
import cells.iterator.range.InverseRangeIterator;
import cells.iterator.range.RangeIterationStrategy;
import cells.iterator.range.RangeIterator;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Dmitriy Tseyler
 */
public class CellRange implements Iterable<CellPointer>, Transferable {
    public static final DataFlavor CELL_RANGE_DATA_FLAVOUR = new DataFlavor(CellRange.class, "Range");

    private final CellPointer begin;
    private final CellPointer end;

    public CellRange(int startRow, int startColumn, int endRow, int endColumn) {
        this(new CellPointer(startRow, startColumn), new CellPointer(endRow, endColumn));
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

    public boolean isOneColumnRange() {
        return begin.getColumn() == end.getColumn();
    }

    public boolean isOneRowRange() {
        return begin.getRow() == end.getRow();
    }

    public static CellRange createCellRange(int[] rows, int[] columns) {
        Arrays.sort(rows);
        Arrays.sort(columns);
        CellPointer start = new CellPointer(rows[0], columns[0]);
        CellPointer end = new CellPointer(rows[rows.length - 1], columns[columns.length - 1]);
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

    public Iterator<CellRange> inverseColumnIterator() {
        return new InverseRangeIterator(this, RangeIterationStrategy.COLUMN);
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
}
