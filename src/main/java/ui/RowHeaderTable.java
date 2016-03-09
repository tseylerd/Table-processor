package ui;

import ui.table.SpreadSheetTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class RowHeaderTable extends JTable {
    private static final int DEFAULT_WIDTH = 40;

    private boolean adjusting;

    private final SpreadSheetTable table;
    private int width = DEFAULT_WIDTH;

    public RowHeaderTable(SpreadSheetTable table) {
        super(table.getRowCount(), 1);
        this.table = table;
        table.getSelectionModel().addListSelectionListener(this::viewTableSelectionChanged); // todo: find a better way, may be one selection model
        getSelectionModel().addListSelectionListener(this::rowHeaderSelectionChanged);
        getColumnModel().getColumn(0).setResizable(true);
    }

    private void viewTableSelectionChanged(ListSelectionEvent event) {
        if (adjusting) {
            return;
        }

        adjusting = true;
        int[] rows = table.getSelectedRows();
        clearSelection();
        addRowSelectionInterval(rows[0], rows[rows.length - 1]);
        adjusting = false;
    }

    private void rowHeaderSelectionChanged(ListSelectionEvent event) {
        if (adjusting) {
            return;
        }

        adjusting = true;
        int[] rows = getSelectedRows();
        table.clearSelection();
        table.addRowSelectionInterval(rows[0], rows[rows.length - 1]);
        table.addColumnSelectionInterval(0, table.getColumnCount() - 1);
        adjusting = false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        return row + 1;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return "";
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        Dimension size = super.getPreferredScrollableViewportSize();
        size.width = width;
        return size;
    }
}
