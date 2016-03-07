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

    public Iterator<CellPointer> inverseIterator() {
        return new InverseCellIterator(this);
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
