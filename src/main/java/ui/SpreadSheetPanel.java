package ui;

import ui.preferences.PreferencesPanel;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetPanel extends JPanel {
    public SpreadSheetPanel() {
        super(new BorderLayout());
        SpreadSheetTable table = new SpreadSheetTable();
        JTable rowHeaderTable = new RowHeaderTable(table);
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JButton selectAll = new JButton(new AbstractAction("Select all") {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.setRowSelectionInterval(0, table.getRowCount() - 1);
                table.setColumnSelectionInterval(0, table.getColumnCount() - 1);
            }
        });
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, selectAll);
        scrollPane.setRowHeaderView(rowHeaderTable);
        add(scrollPane, BorderLayout.CENTER);
        add(new PreferencesPanel(table), BorderLayout.EAST);
    }
}
