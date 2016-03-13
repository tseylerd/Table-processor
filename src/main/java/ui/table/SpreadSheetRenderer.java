package ui.table;

import cells.CellValue;
import ui.table.error.Error;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetRenderer extends DefaultTableCellRenderer {

    @Override
    protected void setValue(Object value) {
        CellValue cellValue = (CellValue)value;
        Error error = cellValue.getError();
        if (error != null) {
            setIcon(error.getIcon());
            setText(error.getDescription());
        } else {
            setIcon(null);
            setText(cellValue.getRendererValue());
        }
    }
}
