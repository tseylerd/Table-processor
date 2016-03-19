package ui.preferences.color;

import ui.laf.grid.BorderMode;

import javax.swing.*;

/**
 * Checkbox with {@link BorderMode} inside
 * @author Dmitriy Tseyler
 */
class BorderModeCheckBox extends JCheckBox {
    private final BorderMode mode;

    BorderModeCheckBox(BorderMode mode) {
        super(mode.getGuiName());
        this.mode = mode;
    }

    BorderMode getMode() {
        return mode;
    }
}
