package ui.tabbedpane;

import ui.SpreadSheetPanel;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Main visible interface of table processor. Contains tabs with tables. Holds saved/unsaved state of each tab.
 * Monitors tabs overwriting.
 * By default contains one table with {@value DEFAULT_NAME} name.
 * @author Dmitriy Tseyler
 */
public class MainTabbedPane extends JTabbedPane {
    private static final String CONFIRMATION_TEXT = "Do you want to overwrite existing table? If not, a copy will be created.";
    private static final String COPY = " copy";
    private static final String DEFAULT_NAME = "New table";

    private final Map<String, File> names;
    private final Set<Runnable> saveListeners;

    public MainTabbedPane() {
        names = new HashMap<>();
        saveListeners = new HashSet<>();

        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        setTabPlacement(JTabbedPane.TOP);
        addTable(DEFAULT_NAME, new SpreadSheetTable());
    }

    public boolean addTable(String name, SpreadSheetTable table) {
        boolean needPutFile = true;

        if (names.containsKey(name)) {
            int result = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(), CONFIRMATION_TEXT);
            if (result == JOptionPane.CANCEL_OPTION) {
                return false;
            } else if (result == JOptionPane.YES_OPTION) {
                removeTabAt(indexOfTab(name));
            } else {
                name += COPY;
                needPutFile = false;
            }
        }

        names.put(name, null);
        addTab(name, new SpreadSheetPanel(table));

        int index = indexOfTab(name);
        setTabComponentAt(index, createTabPanel(name));
        setSelectedIndex(index);
        return needPutFile;
    }

    private JPanel createTabPanel(String name) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel label = new JLabel(name);
        panel.add(label, BorderLayout.CENTER);
        panel.add(new CloseTabButton(this, name), BorderLayout.EAST);
        return panel;
    }

    public void saved(File file) {
        names.put(getAccessibleContext().getAccessibleName(), file);
        saveListeners.forEach(Runnable::run);
    }

    public void replaceWithNewName(File file) {
        String name = file.getName();
        names.remove(getAccessibleContext().getAccessibleName());
        setTitleAt(getSelectedIndex(), name);
        getAccessibleContext().setAccessibleName(name);
        setTabComponentAt(getSelectedIndex(), createTabPanel(name));
    }

    public boolean isSaved() {
        return names.get(getAccessibleContext().getAccessibleName()) != null;
    }

    public File getFile() {
        return names.get(getAccessibleContext().getAccessibleName());
    }

    public void addSaveStateListener(Runnable consumer) {
        saveListeners.add(consumer);
    }

    public void removeTable(String name) {
        removeTabAt(indexOfTab(name));
        names.remove(name);
    }
}
