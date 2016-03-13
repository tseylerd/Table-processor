package ui.menu;

import ui.icon.IconConfiguration;
import ui.icon.IconManager;
import ui.laf.ProcessorUIDefaults;

import javax.swing.*;

/**
 * @author Dmitriy Tseyler
 */
public class CloseTabButton extends JButton {
    public CloseTabButton(JTabbedPane tabbedPane, String name) {
        setBackground(ProcessorUIDefaults.TRANPARENT);
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(false);
        setIcon(IconManager.getInstance().getIcon(IconConfiguration.CLOSE));
        addActionListener(e -> {
            int index = tabbedPane.indexOfTab(name);
            tabbedPane.removeTabAt(index);
        });
    }
}
