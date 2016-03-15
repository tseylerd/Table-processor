package ui.menu;

import storage.ImportFormatException;
import storage.TableImporter;
import ui.tabbedpane.MainTabbedPane;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;

/**
 * @author Dmitriy Tseyler
 */
public class OpenMenuItem extends JMenuItem {
    private final MainTabbedPane tabbedPane;
    private final JFileChooser fileChooser;

    public OpenMenuItem(MainTabbedPane tabbedPane) {
        super("Open table");
        this.tabbedPane = tabbedPane;
        fileChooser = new TableFileChooser();
        addActionListener(this::actionPerformed);
    }

    private void actionPerformed(ActionEvent e) {
        int result = fileChooser.showOpenDialog(JOptionPane.getRootFrame());
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooser.getSelectedFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            SpreadSheetTable table = new TableImporter(reader).importTable();
            if (tabbedPane.addTable(file.getName(), table)) {
                tabbedPane.saved(file);
            }
        } catch (IOException | ImportFormatException ex) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Can't open table. Bad file.");
        }
    }

}
