package ui.table.dnd;

import cells.CellPointer;
import cells.CellRange;
import cells.CellValue;
import ui.table.SpreadSheetTable;
import util.Util;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetTransferHandler extends TransferHandler {
    private static final Logger log = Logger.getLogger(SpreadSheetTransferHandler.class.getName());

    private CellPointer beginPointer;
    private final SpreadSheetTable table;

    public SpreadSheetTransferHandler(SpreadSheetTable table) {
        this.table = table;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        int[] rows = table.getSelectedRows();
        int[] columns = table.getSelectedColumns();
        CellPointer begin = new CellPointer(rows[0], columns[0]);
        CellPointer end = new CellPointer(rows[rows.length - 1], columns[columns.length - 1]);
        beginPointer = table.getPointer();
        return new CellRange(begin, end);
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(CellRange.CELL_RANGE_DATA_FLAVOUR);
    }

    @Override
    public boolean importData(TransferSupport support) {
        int row = table.getSelectedRow();
        int column = table.getSelectedColumn();
        try{
            CellRange range = (CellRange) support.getTransferable().getTransferData(CellRange.CELL_RANGE_DATA_FLAVOUR);
            processRangeImport(range, row, column);
        } catch (IOException | UnsupportedFlavorException e) {
            log.warning("Can't import data. " + e.getMessage());
            return false;
        }
        table.repaint(50);
        return true;
    }

    private void processRangeImport(CellRange range, int selectedRow, int selectedColumn) {
        int rowOffset =  selectedRow - beginPointer.getRow();
        int columnOffset = selectedColumn - beginPointer.getColumn();
        Iterator<CellPointer> iterator;
        if (rowOffset == 0) {
            if (columnOffset > 0) {
                iterator = range.inverseColumnRowIterator();
            } else {
                iterator = range.iterator();
            }
        } else if (rowOffset > 0) {
            iterator = range.inverseColumnRowIterator();
        } else {
            iterator = range.iterator();
        }
        while (iterator.hasNext()) {
            CellPointer pointer = iterator.next();
            CellValue value = table.getValueAt(pointer);
            CellPointer newPointer = new CellPointer(pointer, rowOffset, columnOffset);
            table.setValueAt(new CellValue(), pointer);
            Util.move(value, rowOffset, columnOffset);
            table.setValueAt(value, newPointer);
        }
    }
}
