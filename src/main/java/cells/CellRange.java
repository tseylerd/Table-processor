package cells;

import cells.iterator.CellIterator;
import cells.iterator.InverseCellIterator;
import ui.table.dnd.SpreadSheetDataFlavor;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Dmitriy Tseyler
 */
public class CellRange implements Iterable<CellPointer>, Transferable {
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

    public boolean isOneColumnRange() {
        return begin.getColumn() == end.getColumn();
    }

    public boolean isOneRowRange() {
        return begin.getRow() == end.getRow();
    }

    public Iterator<CellPointer> inverseIterator() {
        return new InverseCellIterator(this);
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
        return new DataFlavor[] {SpreadSheetDataFlavor.getInstance()};
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
