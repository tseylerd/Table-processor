package ui.table;

import cells.CellValue;
import ui.text.EditorTextField;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetEditor extends DefaultCellEditor {
    private final EditorTextField field;

    public SpreadSheetEditor() {
        super(new EditorTextField());
        field = (EditorTextField)editorComponent;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        CellValue cellValue = (CellValue)value;
        Component component = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        field.setValue(cellValue);
        return component;
    }

    @Override
    public Object getCellEditorValue() {
        return field.getValue();
    }
}
