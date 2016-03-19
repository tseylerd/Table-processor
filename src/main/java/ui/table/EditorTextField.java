package ui.table;

import cells.CellValue;

import javax.swing.*;

/**
 * @author Dmitriy Tseyler
 */
class EditorTextField extends JTextField {

    public void setValue(CellValue value) {
        setText(value.getEditorValue());
    }

    public CellValue getValue() {
        return new CellValue("", getText());
    }
}
