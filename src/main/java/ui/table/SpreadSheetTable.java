package ui.table;

import cells.pointer.CellPointer;
import cells.CellRange;
import cells.CellValue;
import ui.laf.grid.TableColorModel;
import ui.table.dnd.SpreadSheetTransferHandler;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetTable extends JTable {
    private static final String UI_ID = "SpreadSheetTableUI";
    private static final int DEFAULT_ROW_COUNT = 40;
    private static final int DEFAULT_COLUMN_COUNT = 40;

    private final TableColorModel tableColorModel;

    public SpreadSheetTable() {
        this(DEFAULT_ROW_COUNT, DEFAULT_COLUMN_COUNT);
    }

    public SpreadSheetTable(int rowCount, int columnCount) {
        super(new SpreadSheetModel(rowCount, columnCount));
        tableColorModel = new TableColorModel();
        setCellSelectionEnabled(true);
        setDefaultEditor(CellValue.class, new SpreadSheetEditor());
        setDefaultRenderer(CellValue.class, new SpreadSheetRenderer(this));
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setDragEnabled(true);
        setDropMode(DropMode.USE_SELECTION);
        setTransferHandler(new SpreadSheetTransferHandler(this));
        getTableHeader().setReorderingAllowed(false);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    @Override
    public String getUIClassID() {
        return UI_ID;
    }

    public CellValue getValueAt(CellPointer pointer) {
        return (CellValue)getValueAt(pointer.getRow(), pointer.getColumn());
    }

    public void setValueAt(CellValue value, CellPointer pointer) {
        setValueAt(value, pointer.getRow(), pointer.getColumn());
    }

    public TableColorModel getTableColorModel() {
        return tableColorModel;
    }

    public CellPointer pointerAt(Point point) {
        int row = rowAtPoint(point);
        int column = columnAtPoint(point);
        return CellPointer.getPointer(row, column);
    }

    public void clearSelectedCells() {
        CellRange range = CellRange.createCellRange(getSelectedRows(), getSelectedColumns());
        if (range != null) {
            for (CellPointer cellPointer : range) {
                setValueAt(new CellValue(), cellPointer);
            }
        }
    }

    public void addColumn() {
        ((SpreadSheetModel)getModel()).addColumn(getColumnModel());
    }

    public void addRow() {
        ((SpreadSheetModel)getModel()).addRow();
    }

    public void removeRow() {
        ((SpreadSheetModel)getModel()).removeRow();
    }

    public void removeColumn() {
        ((SpreadSheetModel)getModel()).removeColumn(getColumnModel());
    }
}
