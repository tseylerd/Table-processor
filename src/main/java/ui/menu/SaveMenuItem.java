package ui.menu;

import storage.TableExporter;
import ui.MainTabbedPane;
import ui.SpreadSheetPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Dmitriy Tseyler
 */
public class SaveMenuItem extends JMenuItem {
    private final MainTabbedPane tabbedPane;
    private final JFileChooser fileChooser;

    public SaveMenuItem(MainTabbedPane tabbedPane) {
        super("Save table");
        this.tabbedPane = tabbedPane;
        fileChooser = new TableFileChooser();
        addActionListener(this::actionPerformed);
    }

    private void actionPerformed(ActionEvent e) {
        String name = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
        fileChooser.setSelectedFile(new File(name));
        int result = fileChooser.showSaveDialog(JOptionPane.getRootFrame());
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        SpreadSheetPanel panel = (SpreadSheetPanel)tabbedPane.getSelectedComponent();
        File file = fileChooser.getSelectedFile();
        if (!(file.getName().endsWith(TableFileChooser.FILE_FORMAT))) {
            file = new File(file.getPath() + TableFileChooser.FILE_FORMAT);
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            new TableExporter(panel.getTable(), writer).export();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Can't save this table in file");
        }
    }
}
