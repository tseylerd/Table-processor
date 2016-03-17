package ui.table.dnd;

import cells.pointer.CellPointer;
import cells.CellRange;
import cells.CellValue;
import ui.table.SpreadSheetModel;
import ui.table.SpreadSheetTable;
import ui.table.error.Error;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Transfer handler for {@link SpreadSheetTable} to move ranges of values
 * @author Dmitriy Tseyler
 */
public class SpreadSheetTransferHandler extends TransferHandler {
    private static final Logger log = Logger.getLogger(SpreadSheetTransferHandler.class.getName());

    private final SpreadSheetTable table;

    private int row;
    private int column;
    private CellPointer beginPointer;

    public SpreadSheetTransferHandler(SpreadSheetTable table) {
        this.table = table;
        table.addMouseListener(new BeginCellListener());
    }

    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        int[] rows = table.getSelectedRows();
        int[] columns = table.getSelectedColumns();
        CellPointer begin = CellPointer.getPointer(rows[0], columns[0]);
        CellPointer end = CellPointer.getPointer(rows[rows.length - 1], columns[columns.length - 1]);
        beginPointer = CellPointer.getPointer(row, column);
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

    /**
     * Here we import range from one place to another.
     * One important thing: order, in which we have to copy values, depends on direction of motion
     * @see {@link cells.iterator.cell.CellIterationStrategy}
     * @see {@link cells.iterator.cell.CellIterator}
     */
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
        SpreadSheetModel model = (SpreadSheetModel)table.getModel();
        while (iterator.hasNext()) {
            CellPointer pointer = iterator.next();
            CellValue value = table.getValueAt(pointer);
            CellPointer newPointer = CellPointer.getPointer(pointer, rowOffset, columnOffset);
            table.setValueAt(new CellValue(), pointer);
            if (!(value.getError() == Error.PARSE)) {
                Util.move(value, rowOffset, columnOffset, model.getRowCount(), model.getColumnCount());
            }
            table.setValueAt(value, newPointer);
        }
    }

    private class BeginCellListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            Point point = e.getPoint();
            row = table.rowAtPoint(point);
            column = table.columnAtPoint(point);
        }
    }
}
