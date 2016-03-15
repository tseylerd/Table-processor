package ui.laf.grid;

import ui.laf.ProcessorUIDefaults;

import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class CellColorModel {
    public static final Color DEFAULT_GRID_COLOR = ProcessorUIDefaults.DEFAULT_GRID_COLOR;
    public static final Color DEFAULT_BACKGROUND_COLOR = ProcessorUIDefaults.DEFAULT_BACKGROUND_COLOR;

    private Color lowerColor;
    private Color rightColor;
    private Color background;
    private boolean needDownLine;
    private boolean needRightLine;

    public CellColorModel() {
        lowerColor = DEFAULT_GRID_COLOR;
        rightColor = DEFAULT_GRID_COLOR;
        background = DEFAULT_BACKGROUND_COLOR;

        needDownLine = true;
        needRightLine = true;
    }

    public boolean isNeedLowerLine() {
        return needDownLine;
    }

    public boolean isNeedRightLine() {
        return needRightLine;
    }

    public Color getRightColor() {
        return rightColor;
    }

    public Color getLowerColor() {
        return lowerColor;
    }

    public void setLowerColor(Color lowerColor) {
        this.lowerColor = lowerColor;
    }

    public void setRightColor(Color rightColor) {
        this.rightColor = rightColor;
    }

    public void setNeedLowerLine(boolean needDownLine) {
        this.needDownLine = needDownLine;
    }

    public void setNeedRightLine(boolean needRightLine) {
        this.needRightLine = needRightLine;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public void reset() {
        setBackground(DEFAULT_BACKGROUND_COLOR);
        setLowerColor(DEFAULT_GRID_COLOR);
        setRightColor(DEFAULT_GRID_COLOR);
        setNeedLowerLine(true);
        setNeedRightLine(true);
    }
}
