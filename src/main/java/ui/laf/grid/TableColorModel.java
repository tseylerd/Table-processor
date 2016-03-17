package ui.laf.grid;

import cells.CellRange;
import cells.BorderModesMapper;
import cells.ColorMapper;
import cells.pointer.CellPointer;
import storage.LazyDynamicArray;
import ui.laf.ProcessorUIDefaults;
import ui.table.SpreadSheetModel;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Dmitriy Tseyler
 */
public class TableColorModel {
    private static final CellColorModel DEFAULT = new CellColorModel();

    private final BorderModesMapper rightBorderMapper;
    private final BorderModesMapper downBorderMapper;
    private final ColorMapper backgroundMapper;
    private final ColorMapper rightForegroundMapper;
    private final ColorMapper downForegroundMapper;


    public TableColorModel() {
        rightBorderMapper = new BorderModesMapper();
        downBorderMapper = new BorderModesMapper();
        backgroundMapper = new ColorMapper(ProcessorUIDefaults.DEFAULT_BACKGROUND_COLOR);
        rightForegroundMapper = new ColorMapper(ProcessorUIDefaults.DEFAULT_GRID_COLOR);
        downForegroundMapper = new ColorMapper(ProcessorUIDefaults.DEFAULT_GRID_COLOR);
    }

    public void setNeedRightLine(CellRange range, boolean need) {
        Boolean modes = rightBorderMapper.get(range);
        if (modes != need) {
            rightBorderMapper.set(range, need);
        }
    }

    public void setNeedDownLine(CellRange range, boolean need) {
        Boolean modes = downBorderMapper.get(range);
        if (modes != need) {
            downBorderMapper.set(range, need);
        }
    }

    public Boolean getNeedLowerLine(CellRange range) {
        Boolean result = downBorderMapper.get(range);
        return result == null ? true : result;
    }

    public Boolean getNeedRightLine(CellRange range) {
        Boolean result = rightBorderMapper.get(range);
        return result == null ? true : result;
    }

    public void setRangeLineColor(CellRange range, Color color) {
        if (range.getFirstColumn() > 0) {
            CellRange left = new CellRange(range.getFirstRow(), range.getFirstColumn() - 1, range.getLastRow(), range.getFirstColumn() - 1);
            rightForegroundMapper.set(left, color);
        }
        if (range.getFirstRow() > 0) {
            CellRange top = new CellRange(range.getFirstRow() - 1, range.getFirstColumn(), range.getFirstRow() - 1, range.getLastColumn());
            downForegroundMapper.set(top, color);
        }
        rightForegroundMapper.set(range, color);
        downForegroundMapper.set(range, color);
    }

    public void setDownForeground(CellRange range, Color color) {
        downForegroundMapper.set(range, color);
    }

    public Color getBackgroundColor(CellRange range) {
        return backgroundMapper.get(range);
    }

    public boolean needLowerLine(int row, int column) {
        return needLowerLine(CellPointer.getPointer(row, column));
    }

    public boolean needLowerLine(CellPointer pointer) {
        return downBorderMapper.need(pointer);
    }

    public boolean needRightLine(int row, int column) {
        return needRightLine(CellPointer.getPointer(row, column));
    }

    public boolean needRightLine(CellPointer pointer) {
        return rightBorderMapper.need(pointer);
    }

    public Color getRightLineColor(int row, int column) {
        return getRightLineColor(CellPointer.getPointer(row, column));
    }

    public Color getRightLineColor(CellPointer pointer) {
        return rightForegroundMapper.getColor(pointer);
    }

    public Color getLowerLineColor(int row, int column) {
        return getLowerLineColor(CellPointer.getPointer(row, column));
    }

    public Color getLowerLineColor(CellPointer pointer) {
        return downForegroundMapper.getColor(pointer);
    }

    public void setBackgroundColor(CellRange range, Color color) {
        backgroundMapper.set(range, color);
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

    public Color getGridColor(CellRange range) {
        if (range == null) {
            return ProcessorUIDefaults.DEFAULT_GRID_COLOR;
        }
        Color color = null;
        if (range.getFirstColumn() > 0) {
            CellRange left = new CellRange(range.getFirstRow(), range.getFirstColumn() - 1, range.getLastRow(), range.getFirstColumn() - 1);
            color = rightForegroundMapper.get(left);
        }
        if (range.getFirstRow() > 0) {
            CellRange up = new CellRange(range.getFirstRow() - 1, range.getFirstColumn(), range.getFirstRow() - 1, range.getLastColumn());
            Color upColor = downForegroundMapper.get(up);
            if (color != null && !color.equals(upColor)) {
                return ProcessorUIDefaults.DEFAULT_GRID_COLOR;
            }
        }
        Color right = rightForegroundMapper.get(range);
        Color down = downForegroundMapper.get(range);
        if (right.equals(down) && (color == null || color.equals(right))) {
            return right;
        }
        return ProcessorUIDefaults.DEFAULT_GRID_COLOR;
    }

    public boolean isConfigured(CellPointer pointer) {
        CellRange range = new CellRange(pointer, pointer);
        return rightBorderMapper.contains(range) || rightForegroundMapper.contains(range) || downForegroundMapper.contains(range)
                || backgroundMapper.contains(range);
    }

    public void reset(CellRange range) {
        downBorderMapper.delete(range);
        rightBorderMapper.delete(range);
        rightForegroundMapper.delete(range);
        downForegroundMapper.delete(range);
        backgroundMapper.delete(range);
    }
}
