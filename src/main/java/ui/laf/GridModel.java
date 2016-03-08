package ui.laf;

import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class GridModel {

    public boolean needUpperLine(int row, int column) {
        return true;
    }

    public boolean needLeftLine(int row, int column) {
        return true;
    }

    public Color getLeftLineColor(int row, int column) {
        return Color.BLACK;
    }

    public Color getRightLineColor(int row, int column) {
        return Color.BLACK;
    }
}
