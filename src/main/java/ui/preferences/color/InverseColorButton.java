package ui.preferences.color;

import util.Util;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class InverseColorButton extends JButton {
    public InverseColorButton(Action action) {
        super(action);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        setForeground(Util.inverse(bg));
    }
}