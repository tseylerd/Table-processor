package ui.table;

import cells.CellValue;

import javax.swing.*;
import java.awt.*;

/**
 * Editor for {@link SpreadSheetTable} with {@link EditorTextField}
 * @author Dmitriy Tseyler
 */
class SpreadSheetEditor extends DefaultCellEditor {
    private final EditorTextField field;

    SpreadSheetEditor() {
        super(new EditorTextField());
        field = (EditorTextField)editorComponent;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        CellValue cellValue = (CellValue)value;
        field.setValue(cellValue);
        return field;
    }

    @Override
    public Object getCellEditorValue() {
        return field.getValue();
    }
}
