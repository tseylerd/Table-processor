package ui.laf;

import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class GridModel {

    public boolean needLowerLine(int row, int column) {
        return true;
    }

    public boolean needRightLine(int row, int column) {
        return true;
    }

    public Color getRightLineColor(int row, int column) {
        return Color.BLACK;
    }

    public Color getLowerLineColor(int row, int column) {
        return Color.BLACK;
    }
}
