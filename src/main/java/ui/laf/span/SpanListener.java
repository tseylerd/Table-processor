package ui.laf.span;

import cells.CellPointer;
import cells.CellRange;
import ui.laf.span.managers.SpanManager;
import ui.table.SpreadSheetModel;
import ui.table.SpreadSheetTable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

/**
 * @author Dmitriy Tseyler
 */
public class SpanListener implements MouseListener, MouseMotionListener {
    private SpreadSheetTable table;

    private SpanManager spanManager;
    private SpanMode spanMode;
    private CellRange startRange;

    public void install(SpreadSheetTable table) {
        this.table = table;
        table.addMouseListener(this);
        table.addMouseMotionListener(this);
    }

    public void uninstall(SpreadSheetTable table) {
        table.removeMouseListener(this);
        table.removeMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (spanManager == null) {
            return;
        }

        CellPointer current = table.pointerAt(e.getPoint());
        spanManager.newCell(current);
        table.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = e.getPoint();
        int row = table.rowAtPoint(point);
        int column = table.columnAtPoint(point);
        Rectangle rect = table.getCellRect(row, column, true);
        CellPointer pointer = new CellPointer(row, column);

        spanMode = SpanMode.getSpanMode(point, rect, pointer, table);
        if (spanMode == null) {
            table.setCursor(Cursor.getDefaultCursor());
            return;
        }

        table.setCursor(spanMode.getCursor());
        CellPointer start = spanMode.getStartCell(pointer);
        startRange = getStartRange(start);
    }

    private CellRange getStartRange(CellPointer start) {
        if (table.isCellSelected(start.getRow(), start.getColumn())) {
            int[] selectedRows = table.getSelectedRows();
            int[] selectedColumns = table.getSelectedColumns();
            return CellRange.createCellRange(selectedRows, selectedColumns);
        } else {
            return new CellRange(start, start);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (spanMode != null) {
            spanManager = spanMode.createManager();
            spanManager.setStartRange(startRange);
            turnOffTable();
        }
    }

    private void turnOffTable() {
        table.setDragEnabled(false);
        table.clearSelection();
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.repaint();
    }

    private void turnOnTable() {
        table.setDragEnabled(true);
        table.setColumnSelectionAllowed(true);
        table.setRowSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        table.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        spanManager.move((SpreadSheetModel) table.getModel());
        spanManager = null;
        turnOnTable();
    }


    public boolean isCellInside(int row, int column) {
        if (spanManager == null)
            return false;

        CellPointer pointer = new CellPointer(row, column);
        return spanManager.isInside(pointer);
    }
}