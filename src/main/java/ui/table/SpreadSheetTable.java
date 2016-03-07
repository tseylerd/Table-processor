package ui.table;

import cells.CellPointer;
import cells.CellValue;
import ui.table.dnd.SpreadSheetTransferHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetTable extends JTable {
    private static final int DEFAULT_ROW_COUNT = 40;
    private static final int DEFAULT_COLUMN_COUNT = 40;
    private CellPointer pointer;

    public SpreadSheetTable() {
        this(DEFAULT_ROW_COUNT, DEFAULT_COLUMN_COUNT);
        setCellSelectionEnabled(true);
        setDefaultEditor(CellValue.class, new SpreadSheetEditor());
        setDefaultRenderer(CellValue.class, new SpreadSheetRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setGridColor(Color.GRAY);
        setDragEnabled(true);
        setDropMode(DropMode.USE_SELECTION);
        setTransferHandler(new SpreadSheetTransferHandler(this));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { // TODO: 07.03.16 Move to UI
                Point point = e.getPoint();
                int row = rowAtPoint(point);
                int column = columnAtPoint(point);
                pointer = new CellPointer(row, column);
            }
        });
    }

    public CellPointer getPointer() {
        return pointer;
    }

    public SpreadSheetTable(int rowCount, int columnCount) {
        super(new SpreadSheetModel(rowCount, columnCount));
    }

    public CellValue getValueAt(CellPointer pointer) {
        return (CellValue)getValueAt(pointer.getRow(), pointer.getColumn());
    }

    public void setValueAt(CellValue value, CellPointer pointer) {
        setValueAt(value, pointer.getRow(), pointer.getColumn());
    }
}