package ui.menu;

import ui.tabbedpane.MainTabbedPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Dmitriy Tseyler
 */
public class CreateTableMenuItem extends JMenuItem {
    private final NewTableDialog dialog;

    public CreateTableMenuItem(MainTabbedPane tabbedPane) {
        super("New table");
        dialog = new NewTableDialog(tabbedPane);
        addActionListener(this::actionPerformed);
    }

    private void actionPerformed(ActionEvent e) {
        dialog.createTable();
    }
}
