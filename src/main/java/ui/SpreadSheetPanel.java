package ui;

import ui.preferences.PreferencesPanel;
import ui.table.RowHeaderTable;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Panel with preferences panel and table
 * @author Dmitriy Tseyler
 */
public class SpreadSheetPanel extends JPanel {
    private final SpreadSheetTable table;

    public SpreadSheetPanel(SpreadSheetTable table) {
        super(new BorderLayout());
        this.table = table;
        JTable rowHeaderTable = new RowHeaderTable(table);
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JButton selectAll = new JButton(new AbstractAction("Select all") {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> table.setRowSelectionInterval(0, table.getRowCount() - 1));
                SwingUtilities.invokeLater(() -> table.setColumnSelectionInterval(0, table.getColumnCount() - 1));
                SwingUtilities.invokeLater(table::requestFocus);
            }
        });
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, selectAll);
        scrollPane.setRowHeaderView(rowHeaderTable);
        add(scrollPane, BorderLayout.CENTER);
        add(new PreferencesPanel(table), BorderLayout.EAST);
    }

    public SpreadSheetTable getTable() {
        return table;
    }
}
