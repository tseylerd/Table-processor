package ui.table;

import cells.CellValue;
import cells.pointer.CellPointer;
import ui.laf.grid.TableColorModel;
import ui.table.error.Error;
import util.Util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetRenderer extends DefaultTableCellRenderer {

    private final SpreadSheetTable table;

    public SpreadSheetRenderer(SpreadSheetTable table) {
        this.table = table;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        TableColorModel colorModel = this.table.getTableColorModel();
        CellPointer pointer = CellPointer.getPointer(row, column);
        Color background = colorModel.getBackgroundColor(pointer);
        if (background.equals(getBackground())) {
            return component;
        }
        Color foreground = Util.inverse(background);
        if (isSelected) {
            background = Util.mix(component.getBackground(), background);
        }
        component.setBackground(background);
        component.setForeground(foreground);
        return component;
    }

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
