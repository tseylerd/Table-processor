package ui.table;

import cells.pointer.CellPointer;
import cells.CellRange;
import cells.CellValue;
import ui.laf.GridModel;
import ui.table.dnd.SpreadSheetTransferHandler;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetTable extends JTable {
    private static final String UI_ID = "SpreadSheetTableUI";
    private static final int DEFAULT_ROW_COUNT = 40;
    private static final int DEFAULT_COLUMN_COUNT = 40;

    private final GridModel gridModel;

    public SpreadSheetTable() {
        this(DEFAULT_ROW_COUNT, DEFAULT_COLUMN_COUNT);
    }

    public SpreadSheetTable(int rowCount, int columnCount) {
        super(new SpreadSheetModel(rowCount, columnCount));
        gridModel = new GridModel();
        setCellSelectionEnabled(true);
        setDefaultEditor(CellValue.class, new SpreadSheetEditor());
        setDefaultRenderer(CellValue.class, new SpreadSheetRenderer());
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

    public GridModel getGridModel() {
        return gridModel;
    }

    public CellPointer pointerAt(Point point) {
        int row = rowAtPoint(point);
        int column = columnAtPoint(point);
        return CellPointer.getPointer(row, column);
    }

    public void clearSelectedCells() {
        CellRange range = CellRange.createCellRange(getSelectedRows(), getSelectedColumns());
        for (CellPointer cellPointer : range) {
            setValueAt(new CellValue(), cellPointer);
        }
    }
}
