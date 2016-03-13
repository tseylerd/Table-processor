package ui.preferences.color;

import ui.laf.grid.BorderMode;

import javax.swing.*;

/**
 * @author Dmitriy Tseyler
 */
public class BorderModeCheckBox extends JCheckBox {
    private final BorderMode mode;

    public BorderModeCheckBox(BorderMode mode) {
        super(mode.getGuiName());
        this.mode = mode;
    }

    public BorderMode getMode() {
        return mode;
    }
}
