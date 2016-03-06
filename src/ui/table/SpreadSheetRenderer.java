package ui.table;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetRenderer extends DefaultTableCellRenderer {
    @Override
    protected void setValue(Object value) {
        CellValue cellValue = (CellValue)value;
        setText(cellValue.getRendererValue());
    }
}
