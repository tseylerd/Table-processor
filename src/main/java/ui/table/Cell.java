package ui.table;

import cells.CellValue;

import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class Cell {
    private CellValue value;
    private boolean showBorder;
    private Color borderColor;

    public Cell(CellValue value) {
        this.value = value;
    }

    public void setValue(CellValue value) {
        this.value = value;
    }

    public CellValue getValue() {
        return value;
    }

    public boolean isShowBorder() {
        return showBorder;
    }

    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }


    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }
}
