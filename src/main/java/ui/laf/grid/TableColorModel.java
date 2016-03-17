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

    private final SpreadSheetModel model;
    private final BorderModesMapper rightBorderMapper;
    private final BorderModesMapper downBorderMapper;
    private final ColorMapper backgroundMapper;
    private final ColorMapper rightForegroundMapper;
    private final ColorMapper downForegroundMapper;

    private LazyDynamicArray<CellColorModel> values;

    public TableColorModel(SpreadSheetModel model) {
        this.model = model;
        values = new LazyDynamicArray<>(model.getRowCount(), model.getColumnCount(), CellColorModel.class);
        rightBorderMapper = new BorderModesMapper(model, this);
        downBorderMapper = new BorderModesMapper(model, this);
        backgroundMapper = new ColorMapper(ProcessorUIDefaults.DEFAULT_BACKGROUND_COLOR);
        rightForegroundMapper = new ColorMapper(ProcessorUIDefaults.DEFAULT_GRID_COLOR);
        downForegroundMapper = new ColorMapper(ProcessorUIDefaults.DEFAULT_GRID_COLOR);
    }

    public void setBorderModes(CellRange range, List<BorderMode> modeList) {
        for (BorderMode mode : BorderMode.values()) {
            mode.setModePreferences(this, range, modeList.contains(mode));
        }
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

    public Boolean getModes(CellRange range) {
        return null;// rightBorderMapper.get(range);
    }

    public Boolean getNeedLowerLine(CellRange range) {
        return downBorderMapper.get(range);
    }

    public Boolean getNeedRightLine(CellRange range) {
        return rightBorderMapper.get(range);
    }

    public Color getBackgroundColor(CellRange range) {
        return backgroundMapper.get(range);
    }

    public void setRightForeground(CellRange range, Color color) {
        rightForegroundMapper.set(range, color);
    }

    public Color getRightForeground(CellRange range) {
        return rightForegroundMapper.get(range);
    }

    public void setDownForeground(CellRange range, Color color) {
        downForegroundMapper.set(range, color);
    }

    public Color getDownForeground(CellRange range) {
        return downForegroundMapper.get(range);
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

    public void setBackgroundColor(CellPointer pointer, Color color) {
        setBackgroundColor(new CellRange(pointer, pointer), color);
    }

    public Color getBackgroundColor(CellPointer pointer) {
        return getBackgroundColor(new CellRange(pointer, pointer));
    }

    public void setRangeLineColor(CellRange range, Color color) {
        if (range.getFirstColumn() > 0) {
            range = new CellRange(range.getFirstRow(), range.getFirstColumn() - 1, range.getLastRow(), range.getLastColumn());
        }
        if (range.getFirstRow() > 0) {
            range = new CellRange(range.getFirstRow() - 1, range.getFirstColumn(), range.getLastRow(), range.getLastColumn());
        }
        rightForegroundMapper.set(range, color);
        downForegroundMapper.set(range, color);
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
        if (range.getFirstColumn() > 0) {
            range = new CellRange(range.getFirstRow(), range.getFirstColumn(), range.getLastRow(), range.getLastColumn());
        }
        if (range.getFirstRow() > 0) {
            range = new CellRange(range.getFirstRow() - 1, range.getFirstColumn(), range.getLastRow(), range.getLastColumn());
        }
        Color right = rightForegroundMapper.get(range);
        Color down = downForegroundMapper.get(range);
        if (right.equals(down)) {
            return ProcessorUIDefaults.DEFAULT_GRID_COLOR;
        }
        return right;
    }

    public boolean isConfigured(CellPointer pointer) {
        CellRange range = new CellRange(pointer, pointer);
        return rightBorderMapper.contains(range) || rightForegroundMapper.contains(range) || downForegroundMapper.contains(range)
                || backgroundMapper.contains(range);
    }

    public void setLowerLineColor(CellPointer pointer, Color color) {
        setDownForeground(new CellRange(pointer, pointer), color);
    }

    public void setRightLineColor(CellPointer pointer, Color color) {
        setRangeLineColor(new CellRange(pointer, pointer), color);
    }

    public void setNeedLowerLine(CellPointer pointer, boolean need) {
        //setNeedLine(pointer, need, BorderModesMapper.DOWN_INDEX);
    }

    public void setNeedRightLine(CellPointer pointer, boolean need) {
        //setNeedLine(pointer, need, BorderModesMapper.DOWN_INDEX);
    }

    private void setNeedLine(CellPointer pointer, boolean need) {
        /*CellRange range = new CellRange(pointer, pointer);
        boolean[] modes = rightBorderMapper.get(range);
        if (modes[index] != need) {
            modes[index] = need;
            rightBorderMapper.set(range, modes);
        }*/
    }

    public void reset(CellRange range) {
        rightBorderMapper.delete(range);
        rightForegroundMapper.delete(range);
        downForegroundMapper.delete(range);
        backgroundMapper.delete(range);
    }
}
