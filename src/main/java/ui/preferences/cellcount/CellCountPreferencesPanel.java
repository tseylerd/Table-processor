package ui.preferences.cellcount;

import ui.table.SpreadSheetModel;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Dmitriy Tseyler
 */
public class CellCountPreferencesPanel extends JPanel {
    private final SpreadSheetTable table;
    private final JButton incrementRow;
    private final JButton incrementColumn;
    private final JButton decrementRow;
    private final JButton decrementColumn;

    public CellCountPreferencesPanel(SpreadSheetTable table) {
        super(new GridBagLayout());
        this.table = table;

        incrementRow = new JButton(new AbstractAction("Add row") {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.addRow();
                rowCountChanged();
            }
        });
        incrementColumn = new JButton(new AbstractAction("Add column") {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.addColumn();
                columnCountChanged();
            }
        });
        decrementRow = new JButton(new AbstractAction("Remove last row") {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.removeRow();
                rowCountChanged();
            }
        });
        decrementColumn = new JButton(new AbstractAction("Remove last column") {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.removeColumn();
                columnCountChanged();
            }
        });

        addComponents();
        setBorder(new TitledBorder("Row/column control"));
    }

    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(incrementRow, gbc);
        gbc.gridy++;
        add(decrementRow, gbc);
        gbc.gridy++;
        add(incrementColumn, gbc);
        gbc.gridy++;
        add(decrementColumn, gbc);
        gbc.gridy++;

        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(Box.createGlue(), gbc);
    }

    private void rowCountChanged() {
        boolean enabled = table.getRowCount() > 1;
        decrementRow.setEnabled(enabled);

    }

    private void columnCountChanged() {
        boolean enabled = table.getColumnCount() > 1;
        decrementColumn.setEnabled(enabled);
    }
}
