package ui.table;

import cells.CellPointer;
import cells.CellRange;
import cells.CellValue;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetTransferHandler extends TransferHandler {
    private static final Logger logger = Logger.getLogger(SpreadSheetTransferHandler.class.getName());

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
    protected void exportDone(JComponent source, Transferable data, int action) {
        super.exportDone(source, data, action);
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        SpreadSheetTable table = (SpreadSheetTable)c;
        int[] rows = table.getSelectedRows();
        int[] columns = table.getSelectedColumns();
        CellPointer begin = new CellPointer(rows[0], columns[0]);
        CellPointer end = new CellPointer(rows[rows.length - 1], columns[columns.length - 1]);
        beginPointer = table.getPointer();
        return new CellRange(begin, end);
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        return true;
    }

    @Override
    public boolean importData(TransferSupport support) {
        SpreadSheetTable table = (SpreadSheetTable)support.getComponent();
        int row = table.getSelectedRow();
        int column = table.getSelectedColumn();
        try{
            CellRange range = (CellRange) support.getTransferable().getTransferData(SpreadSheetDataFlavor.getInstance());
            processRangeImport(range, row, column);
        } catch (IOException | UnsupportedFlavorException e) {
            logger.warning("Can't import data. " + e.getMessage());
        }
        table.repaint(50);
        return super.importData(support) || true;
    }

    private void processRangeImport(CellRange range, int selectedRow, int selectedColumn) { // TODO: 07.03.16 Cross ranges
        int rowOffset =  selectedRow - beginPointer.getRow();
        int columnOffset = selectedColumn - beginPointer.getColumn();
        Iterator<CellPointer> iterator;
        if (rowOffset == 0) {
            if (columnOffset > 0) {
                iterator = range.inverseIterator();
            } else {
                iterator = range.iterator();
            }
        } else if (rowOffset > 0) {
            iterator = range.inverseIterator();
        } else {
            iterator = range.iterator();
        }
        while (iterator.hasNext()) {
            CellPointer pointer = iterator.next();
            CellValue value = table.getValueAt(pointer);
            CellPointer newPointer = new CellPointer(pointer, rowOffset, columnOffset);
            table.setValueAt(new CellValue(), pointer);
            table.setValueAt(value, newPointer);
        }
    }
}
