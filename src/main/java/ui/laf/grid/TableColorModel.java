package ui.laf.grid;

import cells.CellRange;
import cells.pointer.CellPointer;
import ui.laf.ProcessorUIDefaults;
import ui.table.SpreadSheetModel;

import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.util.*;

/**
 * @author Dmitriy Tseyler
 */
public class TableColorModel {
    private final CellColorModel[][] modelMap;
    private final SpreadSheetModel model;

    public TableColorModel(SpreadSheetModel model) {
        modelMap = new CellColorModel[model.getRowCount()][];
        this.model = model;
        model.addTableModelListener(this::tableChanged);
    }

    public boolean needLowerLine(int row, int column) {
        return needLowerLine(CellPointer.getPointer(row, column));
    }

    public boolean needLowerLine(CellPointer pointer) {
        CellColorModel colorModel = getCellColorModel(pointer.getRow(), pointer.getColumn());
        return colorModel == null || colorModel.isNeedLowerLine();
    }

    public boolean needRightLine(int row, int column) {
        return needRightLine(CellPointer.getPointer(row, column));
    }

    public boolean needRightLine(CellPointer pointer) {
        CellColorModel colorModel = getCellColorModel(pointer.getRow(), pointer.getColumn());
        return colorModel == null || colorModel.isNeedRightLine();
    }

    public Color getRightLineColor(int row, int column) {
        return getRightLineColor(CellPointer.getPointer(row, column));
    }

    public Color getRightLineColor(CellPointer pointer) {
        CellColorModel colorModel = getCellColorModel(pointer.getRow(), pointer.getColumn());
        return colorModel == null ? ProcessorUIDefaults.DEFAULT_GRID_COLOR : colorModel.getRightColor();
    }

    public Color getLowerLineColor(int row, int column) {
        return getLowerLineColor(CellPointer.getPointer(row, column));
    }

    public Color getLowerLineColor(CellPointer pointer) {
        CellColorModel colorModel = getCellColorModel(pointer.getRow(), pointer.getColumn());
        return colorModel == null ? ProcessorUIDefaults.DEFAULT_GRID_COLOR : colorModel.getLowerColor();
    }

    public void setBackgroundColor(CellRange range, Color color) {
        for (CellPointer pointer : range) {
            setBackgroundColor(pointer, color);
        }
    }

    public void setBackgroundColor(CellPointer pointer, Color color) {
        CellColorModel model = getCellColorModel(pointer);
        model.setBackground(color);
    }

    public Color getBackgroundColor(CellRange range) {
        if (range == null) {
            return ProcessorUIDefaults.DEFAULT_BACKGROUND_COLOR;
        }

        Color color = null;
        for (CellPointer pointer : range) {
            CellColorModel model = getCellColorModel(pointer);
            if (color != null && !color.equals(model.getBackground())) {
                return ProcessorUIDefaults.DEFAULT_BACKGROUND_COLOR;
            }
            color = model.getBackground();
        }
        return color;
    }

    public Color getBackgroundColor(CellPointer pointer) {
        CellColorModel model = getCellColorModel(pointer);
        return model.getBackground();
    }

    public void setRangeLineColor(CellRange range, Color color) {
        CellRange firstRowRange = range.getFirstRowRange();
        if (firstRowRange.getFirstRow() > 0) {
            for (CellPointer cellPointer : firstRowRange) {
                setLowerLineColor(CellPointer.getPointer(cellPointer, -1 , 0), color);
            }
        }
        CellRange firstColumnRange = range.getFirstColumnRange();
        if (firstColumnRange.getFirstColumn() > 0) {
            for (CellPointer cellPointer : firstColumnRange) {
                setRightLineColor(CellPointer.getPointer(cellPointer, 0, -1), color);
            }
        }
        for (CellPointer cellPointer : range) {
            setRightLineColor(cellPointer, color);
            setLowerLineColor(cellPointer, color);
        }
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
        for (CellPointer pointer : range) {
            CellColorModel model = getCellColorModel(pointer);
            Color lower = model.isNeedLowerLine() ? model.getLowerColor() : null;
            Color righter = model.isNeedRightLine() ? model.getRightColor() : null;
            Color lefter = null;
            Color upper = null;
            if (pointer.getRow() > 0 && pointer.getRow() == range.getFirstRow()) {
                CellColorModel upperModel = getCellColorModel(CellPointer.getPointer(pointer, -1, 0));
                upper = upperModel.isNeedLowerLine() ? upperModel.getLowerColor() : null;
            }
            if (pointer.getColumn() > 0 && pointer.getColumn() == range.getFirstColumn()) {
                CellColorModel lefterModel = getCellColorModel(CellPointer.getPointer(pointer, 0, -1));
                lefter = lefterModel.isNeedRightLine() ? lefterModel.getRightColor() : null;
            }
            boolean colorsEquals = isColorsEquals(lower, righter, upper, lefter);
            if (!colorsEquals) {
                return ProcessorUIDefaults.DEFAULT_GRID_COLOR;
            } else if (color == null) {
                color = lower;
            } else if (lower != null && !color.equals(lower)) {
                return ProcessorUIDefaults.DEFAULT_GRID_COLOR;
            }
        }
        if (color == null) {
            return ProcessorUIDefaults.DEFAULT_GRID_COLOR;
        }

        return color;
    }

    public boolean isConfigured(CellPointer pointer) {
        return getCellColorModel(pointer).isConfigured();
    }

    private boolean isColorsEquals(Color... colors) {
        Color current = colors[0];
        for (Color color : colors) {
            if (current == null) {
                current = color;
            } else if (color != null && !color.equals(current)) {
                return false;
            }
        }
        return true;
    }

    public void setLowerLineColor(CellPointer pointer, Color color) {
        CellColorModel cellColorModel = modelMap[pointer.getRow()][pointer.getColumn()];
        if (cellColorModel == null) {
            cellColorModel = new CellColorModel();
            modelMap[pointer.getRow()][pointer.getColumn()] = cellColorModel;
        }
        if (cellColorModel.isNeedLowerLine()) {
            cellColorModel.setLowerColor(color);
        }
    }

    public void setRightLineColor(CellPointer pointer, Color color) {
        CellColorModel cellColorModel = modelMap[pointer.getRow()][pointer.getColumn()];
        if (cellColorModel == null) {
            cellColorModel = new CellColorModel();
            modelMap[pointer.getRow()][pointer.getColumn()] = cellColorModel;
        }
        if (cellColorModel.isNeedRightLine()) {
            cellColorModel.setRightColor(color);
        }
    }

    public void setNeedLowerLine(CellPointer pointer, boolean need) {
        CellColorModel cellColorModel = getCellColorModel(pointer);
        cellColorModel.setNeedLowerLine(need);
    }

    public void setNeedRightLine(CellPointer pointer, boolean need) {
        CellColorModel cellColorModel = getCellColorModel(pointer);
        cellColorModel.setNeedRightLine(need);
    }

    private CellColorModel getCellColorModel(CellPointer pointer) {
        return getCellColorModel(pointer.getRow(), pointer.getColumn());
    }

    public void reset(CellRange range) {
        for (CellPointer pointer : range) {
            CellColorModel model = modelMap[pointer.getRow()][pointer.getColumn()];
            if (model != null) {
                model.reset();
            }
        }
        BorderMode.DOWN.setModePreferences(this, range, true);
        BorderMode.LEFT.setModePreferences(this, range, true);
    }

    private CellColorModel getCellColorModel(int row, int column) {
        if (modelMap[row] == null) {
            modelMap[row] = new CellColorModel[model.getColumnCount()];
        }
        if (modelMap[row][column] == null) {
            modelMap[row][column] = new CellColorModel();
        }
        return modelMap[row][column];
    }

    private void tableChanged(TableModelEvent event) {
        if (model.getRowCount() > modelMap.length || model.getColumnCount() > modelMap[0].length) {
            CellColorModel[][] newValues = new CellColorModel[model.getRowCount()][model.getColumnCount()];
            for (int i = 0; i < modelMap.length; i++) {
                System.arraycopy(modelMap[i], 0, newValues[i], 0, modelMap[i].length);
            }
        }
    }
}
