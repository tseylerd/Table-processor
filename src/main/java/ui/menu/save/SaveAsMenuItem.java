package ui.menu.save;

import ui.menu.TableFileChooser;
import ui.tabbedpane.MainTabbedPane;

import javax.swing.*;
import java.io.File;;

/**
 * @author Dmitriy Tseyler
 */
public class SaveAsMenuItem extends AbstractSaveMenuItem {
    private final JFileChooser chooser;

    public SaveAsMenuItem(MainTabbedPane tabbedPane) {
        super(tabbedPane, "Save as...");
        chooser = new TableFileChooser();
    }

    @Override
    protected File getFile() {
        int result = chooser.showSaveDialog(JOptionPane.getRootFrame());
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return chooser.getSelectedFile();
    }

    @Override
    protected void afterSave(File file) {
        tabbedPane.replaceWithNewName(file);
        super.afterSave(file);
    }
}
