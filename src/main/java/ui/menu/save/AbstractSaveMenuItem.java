package ui.menu.save;

import storage.TableExporter;
import ui.tabbedpane.MainTabbedPane;
import ui.SpreadSheetPanel;
import ui.menu.TableFileChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Dmitriy Tseyler
 */
public abstract class AbstractSaveMenuItem extends JMenuItem {
    protected final MainTabbedPane tabbedPane;

    public AbstractSaveMenuItem(MainTabbedPane tabbedPane, String text) {
        super(text);
        this.tabbedPane = tabbedPane;
        installListeners();
    }

    protected void installListeners() {
        addActionListener(this::actionPerformed);
    }

    private void actionPerformed(ActionEvent e) {
        File file = getFile();
        if (file == null) {
            return;
        }

        SpreadSheetPanel panel = (SpreadSheetPanel)tabbedPane.getSelectedComponent();
        if (!(file.getName().endsWith(TableFileChooser.FILE_FORMAT))) {
            file = new File(file.getPath() + TableFileChooser.FILE_FORMAT);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            new TableExporter(panel.getTable(), writer).export();
            afterSave(file);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Can't save this table in file");
        }
    }

    protected void afterSave(File file) {
        tabbedPane.saved(file);
    }

    protected abstract File getFile();
}
