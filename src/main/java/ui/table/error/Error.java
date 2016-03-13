package ui.table.error;

import ui.icon.IconConfiguration;
import ui.icon.IconManager;

import javax.swing.*;

/**
 * @author Dmitriy Tseyler
 */
public enum Error {
    CYCLIC_REFERENCE(IconManager.getInstance().getIcon(IconConfiguration.CYCLIC_REFERENCE), "Cycle"),
    PARSE(IconManager.getInstance().getIcon(IconConfiguration.WARNING), "Error");

    private final Icon icon;
    private final String description;

    Error(Icon icon, String description) {
        this.icon = icon;
        this.description = description;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }
}
