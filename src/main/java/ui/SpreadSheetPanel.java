package ui;

import ui.preferences.PreferencesPanel;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetPanel extends JPanel {
    public SpreadSheetPanel() {
        super(new BorderLayout());
        SpreadSheetTable table = new SpreadSheetTable();
        JTable rowHeaderTable = new RowHeaderTable(table);
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowHeaderTable.getTableHeader());
        scrollPane.setRowHeaderView(rowHeaderTable);
        add(scrollPane, BorderLayout.CENTER);
        add(new PreferencesPanel(table), BorderLayout.EAST);
    }
}
