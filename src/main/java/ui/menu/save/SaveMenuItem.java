package ui.menu.save;

import ui.tabbedpane.MainTabbedPane;

import javax.swing.event.ChangeEvent;
import java.io.File;

/**
 * @author Dmitriy Tseyler
 */
public class SaveMenuItem extends AbstractSaveMenuItem {
    public SaveMenuItem(MainTabbedPane tabbedPane) {
        super(tabbedPane, "Save");
        setEnabled(tabbedPane.isSaved());
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        tabbedPane.addChangeListener(this::tabbedPaneSelectedTableChanged);
        tabbedPane.addSaveStateListener(this::saved);
    }

    @Override
    protected File getFile() {
        return tabbedPane.getFile();
    }

    private void tabbedPaneSelectedTableChanged(ChangeEvent e) {
        setEnabled(tabbedPane.isSaved());
    }

    private void saved() {
        setEnabled(true);
    }
}
