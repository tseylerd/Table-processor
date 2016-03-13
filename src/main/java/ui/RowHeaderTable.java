package ui;

import ui.table.SpreadSheetTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class RowHeaderTable extends JTable {
    private static final int DEFAULT_WIDTH = 100;

    private boolean adjusting;

    private final SpreadSheetTable table;

    public RowHeaderTable(SpreadSheetTable table) {
        super(table.getRowCount(), 1);
        this.table = table;
        table.getSelectionModel().addListSelectionListener(this::viewTableSelectionChanged); // todo: find a better way, may be one selection model
        getSelectionModel().addListSelectionListener(this::rowHeaderSelectionChanged);
        getColumnModel().getColumn(0).setResizable(true);
        table.getModel().addTableModelListener(e -> {
            while (table.getRowCount() < getRowCount()) {
                ((DefaultTableModel)getModel()).removeRow(getRowCount() - 1);
            }
            while (table.getRowCount() > getRowCount()) {
                ((DefaultTableModel)getModel()).addRow(new Object[]{getRowCount()});
            }
        });
    }

    private void viewTableSelectionChanged(ListSelectionEvent event) {
        if (adjusting) {
            return;
        }

        int[] rows = table.getSelectedRows();
        if (rows.length == 0)
            return;

        adjusting = true;
        clearSelection();
        addRowSelectionInterval(rows[0], rows[rows.length - 1]);
        adjusting = false;
    }

    private void rowHeaderSelectionChanged(ListSelectionEvent event) {
        if (adjusting) {
            return;
        }

        int[] rows = getSelectedRows();
        if (rows.length == 0)
            return;

        adjusting = true;
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
        size.width = DEFAULT_WIDTH;
        return size;
    }
}
