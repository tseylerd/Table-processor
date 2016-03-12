package ui.laf;

import ui.laf.span.SpanListener;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetTableUI extends BasicTableUI {

    private final SpreadSheetTable spreadSheetTable;
    private final SpanListener spanListener;
    private final Action deleteAction;

    private SpreadSheetTableUI(SpreadSheetTable spreadSheetTable) {
        this.spreadSheetTable = spreadSheetTable;
        spanListener = new SpanListener();
        deleteAction = new DeleteAction(spreadSheetTable);
    }

    @SuppressWarnings("unused")
    public static ComponentUI createUI(JComponent c) {
        return new SpreadSheetTableUI((SpreadSheetTable) c);
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        spanListener.install(spreadSheetTable);
    }

    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        spanListener.uninstall(spreadSheetTable);
    }

    @Override
    protected void installKeyboardActions() {
        super.installKeyboardActions();
        spreadSheetTable.getActionMap().put(ProcessorUIDefaults.TABLE_BACK_SPACE_ACTION, deleteAction);
    }

    @Override
    protected void uninstallKeyboardActions() {
        super.uninstallKeyboardActions();
        spreadSheetTable.getActionMap().remove(ProcessorUIDefaults.TABLE_BACK_SPACE_ACTION);
    }

    /**
     * Copy of {@link BasicTableUI} paint
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        Rectangle clip = g.getClipBounds();

        Rectangle bounds = table.getBounds();
        // account for the fact that the graphics has already been translated
        // into the table's bounds
        bounds.x = bounds.y = 0;

        if (table.getRowCount() <= 0 || table.getColumnCount() <= 0 ||
                // this check prevents us from painting the entire table
                // when the clip doesn't intersect our bounds at all
                !bounds.intersects(clip)) {

            paintDropLines(g);
            return;
        }

        boolean ltr = table.getComponentOrientation().isLeftToRight();

        Point upperLeft = clip.getLocation();
        Point lowerRight = new Point(clip.x + clip.width - 1,
                clip.y + clip.height - 1);

        int rMin = table.rowAtPoint(upperLeft);
        int rMax = table.rowAtPoint(lowerRight);
        // This should never happen (as long as our bounds intersect the clip,
        // which is why we bail above if that is the case).
        if (rMin == -1) {
            rMin = 0;
        }
        // If the table does not have enough rows to fill the view we'll get -1.
        // (We could also get -1 if our bounds don't intersect the clip,
        // which is why we bail above if that is the case).
        // Replace this with the index of the last row.
        if (rMax == -1) {
            rMax = table.getRowCount()-1;
        }

        int cMin = table.columnAtPoint(ltr ? upperLeft : lowerRight);
        int cMax = table.columnAtPoint(ltr ? lowerRight : upperLeft);
        // This should never happen.
        if (cMin == -1) {
            cMin = 0;
        }
        // If the table does not have enough columns to fill the view we'll get -1.
        // Replace this with the index of the last column.
        if (cMax == -1) {
            cMax = table.getColumnCount()-1;
        }


        // Paint the grid.
        paintGrid(g, rMin, rMax, cMin, cMax);
        // Paint the cells.
        paintCells(g, rMin, rMax, cMin, cMax);
        paintDropLines(g);
    }

    /**
     * Copy of {@link BasicTableUI}
     */
    private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax) {
        TableColumnModel cm = table.getColumnModel();
        int columnMargin = cm.getColumnMargin();

        Rectangle cellRect;
        TableColumn aColumn;
        int columnWidth;
        for(int row = rMin; row <= rMax; row++) {
            cellRect = table.getCellRect(row, cMin, false);
            for(int column = cMin; column <= cMax; column++) {
                aColumn = cm.getColumn(column);
                columnWidth = aColumn.getWidth();
                cellRect.width = columnWidth - columnMargin;
                paintCell(g, cellRect, row, column);
                cellRect.x += columnWidth;
            }
        }

        rendererPane.removeAll();
    }

    /**
     * Copy of {@link BasicTableUI}
     */
    private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
        if (table.isEditing() && table.getEditingRow()==row &&
                table.getEditingColumn()==column) {
            Component component = table.getEditorComponent();
            component.setBounds(cellRect);
            component.validate();
        } else {
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component component = table.prepareRenderer(renderer, row, column);
            rendererPane.paintComponent(g, component, table, cellRect.x, cellRect.y,
                    cellRect.width, cellRect.height, true);
        }
        if (spanListener.isCellInside(row, column)) {
            g.setColor(new Color(17, 17, 17, 50));
            g.fillRect(cellRect.x, cellRect.y, cellRect.width, cellRect.height);
        }
    }

    private void paintHover(Graphics g, Rectangle cellRect, int row, int column) {
        Point point = table.getMousePosition();
        if (point == null) {
            return;
        }

        int hoveredRow = table.rowAtPoint(point);
        int hoveredColumn = table.columnAtPoint(point);
        if (hoveredRow == row && hoveredColumn == column) {
            g.setColor(new Color(17, 17, 17, 50));
            g.fillRect(cellRect.x, cellRect.y, cellRect.width, cellRect.height);
        }
    }

    /**
     * Copy of {@link BasicTableUI}, except one moment: we want to draw line depends on grid model values
     */
    private void paintGrid(Graphics g, int rMin, int rMax, int cMin, int cMax) {
        Rectangle minCell = table.getCellRect(rMin, cMin, true);
        Rectangle maxCell = table.getCellRect(rMax, cMax, true);
        Rectangle damagedArea = minCell.union(maxCell);
        GridModel gridModel = spreadSheetTable.getGridModel();

        TableColumnModel columnModel = table.getColumnModel();

        int y = damagedArea.y;
        for (int row = rMin; row <= rMax; row++) {
            int x = damagedArea.x;
            y += table.getRowHeight(row);
            for (int column = cMin; column <= cMax; column++) {
                if (gridModel.needLowerLine(row, column)) {
                    Color color = gridModel.getLowerLineColor(row, column);
                    g.setColor(color);
                    g.drawLine(x, y - 1, x + columnModel.getColumn(column).getWidth() - 1, y - 1);
                }
                x += columnModel.getColumn(column).getWidth();
            }
        }
        int x = damagedArea.x;
        for (int column = cMin; column <= cMax; column++) {
            y = 0;
            x += columnModel.getColumn(column).getWidth();
            for (int row = 0; row <= rMax; row++) {
                if (gridModel.needRightLine(row, column)) {
                    Color color = gridModel.getRightLineColor(row, column);
                    g.setColor(color);
                    g.drawLine(x - 1, y, x - 1, y + table.getRowHeight(row) - 1);
                }
                y += table.getRowHeight();
            }
        }
    }

    /**
     * Copy of {@link BasicTableUI}
     */
    private void paintDropLines(Graphics g) {
        JTable.DropLocation loc = table.getDropLocation();
        if (loc == null) {
            return;
        }

        Color color = UIManager.getColor("Table.dropLineColor");
        Color shortColor = UIManager.getColor("Table.dropLineShortColor");
        if (color == null && shortColor == null) {
            return;
        }

        Rectangle rect;

        rect = getHDropLineRect(loc);
        if (rect != null) {
            int x = rect.x;
            int w = rect.width;
            if (color != null) {
                extendRect(rect, true);
                g.setColor(color);
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
            if (!loc.isInsertColumn() && shortColor != null) {
                g.setColor(shortColor);
                g.fillRect(x, rect.y, w, rect.height);
            }
        }

        rect = getVDropLineRect(loc);
        if (rect != null) {
            int y = rect.y;
            int h = rect.height;
            if (color != null) {
                extendRect(rect, false);
                g.setColor(color);
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
            if (!loc.isInsertRow() && shortColor != null) {
                g.setColor(shortColor);
                g.fillRect(rect.x, y, rect.width, h);
            }
        }
    }

    /**
     * Copy of {@link BasicTableUI}
     */
    private Rectangle getHDropLineRect(JTable.DropLocation loc) {
        if (!loc.isInsertRow()) {
            return null;
        }

        int row = loc.getRow();
        int col = loc.getColumn();
        if (col >= table.getColumnCount()) {
            col--;
        }

        Rectangle rect = table.getCellRect(row, col, true);

        if (row >= table.getRowCount()) {
            row--;
            Rectangle prevRect = table.getCellRect(row, col, true);
            rect.y = prevRect.y + prevRect.height;
        }

        if (rect.y == 0) {
            rect.y = -1;
        } else {
            rect.y -= 2;
        }

        rect.height = 3;

        return rect;
    }

    private Rectangle getVDropLineRect(JTable.DropLocation loc) {
        if (!loc.isInsertColumn()) {
            return null;
        }

        boolean ltr = table.getComponentOrientation().isLeftToRight();
        int col = loc.getColumn();
        Rectangle rect = table.getCellRect(loc.getRow(), col, true);

        if (col >= table.getColumnCount()) {
            col--;
            rect = table.getCellRect(loc.getRow(), col, true);
            if (ltr) {
                rect.x = rect.x + rect.width;
            }
        } else if (!ltr) {
            rect.x = rect.x + rect.width;
        }

        if (rect.x == 0) {
            rect.x = -1;
        } else {
            rect.x -= 2;
        }

        rect.width = 3;

        return rect;
    }

    /**
     * Copy of {@link BasicTableUI}
     */
    private Rectangle extendRect(Rectangle rect, boolean horizontal) {
        if (rect == null) {
            return null;
        }

        if (horizontal) {
            rect.x = 0;
            rect.width = table.getWidth();
        } else {
            rect.y = 0;

            if (table.getRowCount() != 0) {
                Rectangle lastRect = table.getCellRect(table.getRowCount() - 1, 0, true);
                rect.height = lastRect.y + lastRect.height;
            } else {
                rect.height = table.getHeight();
            }
        }

        return rect;
    }

    private static class DeleteAction extends AbstractAction {
        private final SpreadSheetTable table;

        DeleteAction(SpreadSheetTable table) {
            this.table = table;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            table.clearSelectedCells();
        }
    }
}