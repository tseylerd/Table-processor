package ui.menu;

import ui.tabbedpane.MainTabbedPane;
import ui.laf.ProcessorUIDefaults;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Dmitriy Tseyler
 */
public class NewTableDialog extends JDialog {
    private static final String DEFAULT_TAB_NAME = "New table";

    private final MainTabbedPane tabbedPane;
    private final JSpinner rowCount;
    private final JSpinner columnCount;
    private final JTextField name;

    public NewTableDialog(MainTabbedPane tabbedPane) {
        super(JOptionPane.getRootFrame(), true);
        this.tabbedPane = tabbedPane;

        JPanel contentPane = new JPanel(new GridLayout(4, 1));
        rowCount = new JSpinner(new SpinnerNumberModel(ProcessorUIDefaults.DEFAULT_ROW_COUNT, 1, Short.MAX_VALUE, 1));
        columnCount = new JSpinner(new SpinnerNumberModel(ProcessorUIDefaults.DEFAULT_COLUMN_COUNT, 1, Short.MAX_VALUE, 1));
        name = new JTextField();
        contentPane.add(createLabeledComponent("Table name: ", name));
        contentPane.add(createLabeledComponent("Row count: ", rowCount));
        contentPane.add(createLabeledComponent("Column count: ", columnCount));
        contentPane.add(getControlPanel());
        setContentPane(contentPane);
    }

    private JPanel createLabeledComponent(String name, Component component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(name), BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private JPanel getControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton create = new JButton(new AbstractAction("Create") {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.addTable(name.getText(), new SpreadSheetTable(getRowCount(), getColumnCount()));
                setVisible(false);
            }
        });
        panel.add(create, BorderLayout.WEST);
        panel.add(new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        }), BorderLayout.EAST);
        getRootPane().setDefaultButton(create);
        return panel;
    }

    public int getRowCount() {
        return (Integer) rowCount.getValue();
    }

    public int getColumnCount() {
        return (Integer) columnCount.getValue();
    }

    public String getName() {
        return name.getText();
    }

    public void createTable() {
        name.setText(DEFAULT_TAB_NAME);
        rowCount.setValue(ProcessorUIDefaults.DEFAULT_ROW_COUNT);
        columnCount.setValue(ProcessorUIDefaults.DEFAULT_COLUMN_COUNT);
        pack();
        setVisible(true);
    }
}
