package main.java.ui.text;

import main.java.cells.CellValue;

import javax.swing.*;

/**
 * @author Dmitriy Tseyler
 */
public class EditorTextField extends JTextField {
    private CellValue value;

    public void setValue(CellValue value) {
        this.value = value;
        setText(value.getEditorValue());
    }

    public CellValue getValue() {
        value.setExpression(getText());
        return value;
    }
}
