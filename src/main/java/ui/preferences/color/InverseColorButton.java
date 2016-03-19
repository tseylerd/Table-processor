package ui.preferences.color;

import util.Util;

import javax.swing.*;
import java.awt.*;

/**
 * Button with always visible text color depends on background color
 * @author Dmitriy Tseyler
 */
class InverseColorButton extends JButton {
    InverseColorButton(Action action) {
        super(action);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        setForeground(Util.inverse(bg));
    }
}