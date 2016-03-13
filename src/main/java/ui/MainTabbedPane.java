package ui;

import ui.menu.CloseTabButton;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class MainTabbedPane extends JTabbedPane {
    public MainTabbedPane() {
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        setTabPlacement(JTabbedPane.TOP);
        addTable("Default", new SpreadSheetTable());
    }

    public void addTable(String name, SpreadSheetTable table) {
        addTab(name, new SpreadSheetPanel(table));
        setTabComponentAt(indexOfTab(name), createTabPanel(name));
    }

    private JPanel createTabPanel(String name) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel label = new JLabel(name);
        panel.add(label, BorderLayout.CENTER);
        panel.add(new CloseTabButton(this, name), BorderLayout.EAST);
        return panel;
    }
}
