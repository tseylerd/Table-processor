package ui.preferences.cellcount;

import ui.laf.ProcessorUIDefaults;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Panel for customize rows and columns count
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
        table.getModel().addTableModelListener(this::tableModelChanged);
        tableModelChanged(null);
    }

    private void tableModelChanged(TableModelEvent e) {
        decrementRow.setEnabled(table.getModel().getRowCount() > 1);
        decrementColumn.setEnabled(table.getModel().getColumnCount() > 1);
        incrementRow.setEnabled(table.getModel().getRowCount() < ProcessorUIDefaults.MAX_ROWS_COLUMNS);
        incrementColumn.setEnabled(table.getModel().getColumnCount() < ProcessorUIDefaults.MAX_ROWS_COLUMNS);
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
