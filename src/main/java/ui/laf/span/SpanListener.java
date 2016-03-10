package ui.laf.span;

import cells.CellPointer;
import cells.CellRange;
import ui.laf.span.mode.SpanManager;
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
    private CellPointer last;
    private int[] selectedRows;
    private int[] selectedColumns;

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
        if (!current.equals(last)) {
            last = current;
            spanManager.newCell(current);
        }
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
        int rectLeftX = rect.x;
        int rectUpperY = rect.y;
        int rectRightX = rectLeftX + rect.width;
        int rectDownY = rectUpperY + rect.height;

        boolean onLeftBorder = compare(rectLeftX, point.x);
        boolean onRightBorder = compare(rectRightX, point.x);
        boolean onDownBorder = compare(rectDownY, point.y);
        boolean onUpperBorder = compare(rectUpperY, point.y);
        spanManager = SpanManager.getMode(onLeftBorder, onRightBorder, onUpperBorder, onDownBorder);
        if (spanManager != null) {
            table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (table.isCellSelected(row, column)) {
                selectedRows = table.getSelectedRows();
                selectedColumns = table.getSelectedColumns();
            } else {
                selectedRows = null;
                selectedColumns = null;
            }
            if (last != null) {
                table.repaint(table.getCellRect(last.getRow(), last.getColumn(), true));
            }
            last = new CellPointer(row, column);
            table.repaint(table.getCellRect(row, column, true));
        } else {
            last = null;
            table.setCursor(Cursor.getDefaultCursor());
            selectedRows = null;
            selectedColumns = null;
            table.repaint();
        }
    }

    private static boolean compare(int rectCoordinate, int mouseCoordinate) {
        return Math.abs(rectCoordinate - mouseCoordinate) < 5;
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (spanManager != null) {
            CellPointer pointer = table.pointerAt(e.getPoint());
            CellRange range = getStartRange(pointer);
            spanManager.setStartRange(range);
            last = pointer;
            spanManager.newCell(pointer);
            table.setDragEnabled(false);
            table.clearSelection();
            table.setColumnSelectionAllowed(false);
            table.setRowSelectionAllowed(false);
            table.setCellSelectionEnabled(false);
            table.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        spanManager = null;
        last = null;
        table.setDragEnabled(true);
        table.setColumnSelectionAllowed(true);
        table.setRowSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        table.repaint();
    }

    private CellRange getStartRange(CellPointer pointer) {
        CellPointer start = pointer;
        CellPointer end = pointer;
        if (selectedRows != null) {
            Arrays.sort(selectedRows);
            Arrays.sort(selectedColumns);
            start = new CellPointer(selectedRows[0], selectedColumns[0]);
            end = new CellPointer(selectedRows[selectedRows.length - 1], selectedColumns[selectedColumns.length - 1]);
        }
        return new CellRange(start, end);
    }

    public boolean isCellInside(int row, int column) {
        if (spanManager == null)
            return false;

        CellPointer pointer = new CellPointer(row, column);
        return spanManager.isInside(pointer);
    }
}
