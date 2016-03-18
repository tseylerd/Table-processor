package ui.laf.grid;

import cells.CellRange;
import cells.BorderModesMapper;
import cells.ColorMapper;
import cells.pointer.CellPointer;
import ui.laf.ProcessorUIDefaults;

import java.awt.*;
import java.util.*;

/**
 * This is the table color model. It holds colors of grid lines and grid showing parameters
 * @author Dmitriy Tseyler
 */
public class TableColorModel {
    private final BorderModesMapper rightLineMapper;
    private final BorderModesMapper bottomLineMapper;
    private final ColorMapper backgroundMapper;
    private final ColorMapper rightForegroundMapper;
    private final ColorMapper bottomForegroundMapper;


    public TableColorModel() {
        rightLineMapper = new BorderModesMapper();
        bottomLineMapper = new BorderModesMapper();
        backgroundMapper = new ColorMapper(ProcessorUIDefaults.DEFAULT_BACKGROUND_COLOR);
        rightForegroundMapper = new ColorMapper(ProcessorUIDefaults.DEFAULT_GRID_COLOR);
        bottomForegroundMapper = new ColorMapper(ProcessorUIDefaults.DEFAULT_GRID_COLOR);
    }

    // ranges setters/getters

    public void setNeedRightLine(CellRange range, boolean need) {
        Boolean modes = rightLineMapper.get(range);
        if (modes != need) { // replaceable by xor, but more friendly
            rightLineMapper.set(range, need);
        }
    }

    public void setNeedBottomLine(CellRange range, boolean need) {
        Boolean modes = bottomLineMapper.get(range);
        if (modes != need) {
            bottomLineMapper.set(range, need);
        }
    }

    public Boolean getNeedBottomLine(CellRange range) {
        Boolean result = bottomLineMapper.get(range);
        return result == null ? true : result;
    }

    public Boolean getNeedRightLine(CellRange range) {
        Boolean result = rightLineMapper.get(range);
        return result == null ? true : result;
    }

    public Color getBackgroundColor(CellRange range) {
        Color color = backgroundMapper.get(range);
        return color == null ? ProcessorUIDefaults.DEFAULT_BACKGROUND_COLOR : color;
    }

    public void setBackgroundColor(CellRange range, Color color) {
        backgroundMapper.set(range, color);
    }

    public Color getGridColor(CellRange range) {
        if (range == null) {
            return ProcessorUIDefaults.DEFAULT_GRID_COLOR;
        }
        Color color = null;
        if (range.getFirstColumn() > 0) { // check if we are on top
            CellRange left = new CellRange(range.getFirstRow(), range.getFirstColumn() - 1, range.getLastRow(), range.getFirstColumn() - 1);
            color = rightForegroundMapper.get(left);
        }
        if (range.getFirstRow() > 0) { // check if we are in left
            CellRange up = new CellRange(range.getFirstRow() - 1, range.getFirstColumn(), range.getFirstRow() - 1, range.getLastColumn());
            Color upColor = bottomForegroundMapper.get(up);

            if (color != null && !color.equals(upColor)) {
                return ProcessorUIDefaults.DEFAULT_GRID_COLOR;
            }
        }

        Color right = rightForegroundMapper.get(range);
        Color down = bottomForegroundMapper.get(range);
        if (right.equals(down) && (color == null || color.equals(right))) {
            return right;
        }

        return ProcessorUIDefaults.DEFAULT_GRID_COLOR;
    }

    public void setGridColor(CellRange range, Color color) {
        if (range.getFirstColumn() > 0) {
            CellRange left = new CellRange(range.getFirstRow(), range.getFirstColumn() - 1, range.getLastRow(), range.getFirstColumn() - 1);
            rightForegroundMapper.set(left, color);
        }
        if (range.getFirstRow() > 0) {
            CellRange top = new CellRange(range.getFirstRow() - 1, range.getFirstColumn(), range.getFirstRow() - 1, range.getLastColumn());
            bottomForegroundMapper.set(top, color);
        }
        rightForegroundMapper.set(range, color);
        bottomForegroundMapper.set(range, color);
    }

    // row/column getters/setters

    public boolean needBottomLine(int row, int column) {
        return needBottomLine(CellPointer.getPointer(row, column));
    }

    public boolean needBottomLine(CellPointer pointer) {
        return bottomLineMapper.need(pointer);
    }

    public boolean needRightLine(int row, int column) {
        return needRightLine(CellPointer.getPointer(row, column));
    }

    public boolean needRightLine(CellPointer pointer) {
        return rightLineMapper.need(pointer);
    }

    public Color getRightLineColor(int row, int column) {
        return getRightLineColor(CellPointer.getPointer(row, column));
    }

    public Color getRightLineColor(CellPointer pointer) {
        return rightForegroundMapper.getColor(pointer);
    }

    public Color getBottomLineColor(int row, int column) {
        return getBottomLineColor(CellPointer.getPointer(row, column));
    }

    public Color getBottomLineColor(CellPointer pointer) {
        return bottomForegroundMapper.getColor(pointer);
    }

    public Color getBackgroundColor(CellPointer pointer) {
        return getBackgroundColor(new CellRange(pointer, pointer));
    }

    public Map<BorderMode, Boolean> getBorderModesTurnedOnMap(CellRange range) {
        Map<BorderMode, Boolean> modesMap = new EnumMap<>(BorderMode.class);
        for (BorderMode mode : BorderMode.values()) {
            modesMap.put(mode, mode.isModeTurnedOn(this, range));
        }
        return modesMap;
    }

    public Map<CellRange, Boolean> getBottomLineMap() {
        return bottomLineMapper.getMap();
    }

    public Map<CellRange, Boolean> getRightLineMap() {
        return rightLineMapper.getMap();
    }

    public Map<CellRange, Color> getBackgroundMap() {
        return backgroundMapper.getMap();
    }

    public Map<CellRange, Color> getRightLineColorMap() {
        return rightForegroundMapper.getMap();
    }

    public Map<CellRange, Color> getBottomLineColorMap() {
        return bottomForegroundMapper.getMap();
    }

    public void reset(CellRange range) {
        bottomLineMapper.delete(range);
        rightLineMapper.delete(range);
        rightForegroundMapper.delete(range);
        bottomForegroundMapper.delete(range);
        backgroundMapper.delete(range);
    }

    public void setMaps(Map<CellRange, Color> rightLineColors, Map<CellRange, Color> bottomLinecColors,
                        Map<CellRange, Color> backgroundColors, Map<CellRange, Boolean> rightLines,
                        Map<CellRange, Boolean> bottomLines)
    {
        rightForegroundMapper.setMap(rightLineColors);
        bottomForegroundMapper.setMap(bottomLinecColors);
        backgroundMapper.setMap(backgroundColors);
        rightLineMapper.setMap(rightLines);
        bottomLineMapper.setMap(bottomLines);
    }
}
