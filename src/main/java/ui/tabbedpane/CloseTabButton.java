package ui.tabbedpane;

import ui.icon.IconConfiguration;
import ui.icon.IconManager;
import ui.laf.ProcessorUIDefaults;

import javax.swing.*;

/**
 * This button deletes tab from {@link MainTabbedPane}
 * @author Dmitriy Tseyler
 */
class CloseTabButton extends JButton {
    CloseTabButton(MainTabbedPane tabbedPane, String name) {
        setBackground(ProcessorUIDefaults.TRANSPARENT);
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(false);
        setIcon(IconManager.getInstance().getIcon(IconConfiguration.CLOSE));
        addActionListener(e -> {
            if (tabbedPane.getTabCount() > 1) {
                tabbedPane.removeTable(name);
            }
        });
    }
}
