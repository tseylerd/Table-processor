package ui.laf.grid;

import cells.CellRange;
import cells.RangeAggregator;
import cells.pointer.CellPointer;
import storage.LazyDynamicArray;
import ui.laf.ProcessorUIDefaults;
import ui.table.SpreadSheetModel;

import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Dmitriy Tseyler
 */
public class TableColorModel {
    private static final CellColorModel DEFAULT = new CellColorModel();

    private final SpreadSheetModel model;
    private final RangeAggregator aggregator;

    private LazyDynamicArray<CellColorModel> values;

    public TableColorModel(SpreadSheetModel model) {
        this.model = model;
        values = new LazyDynamicArray<>(model.getRowCount(), model.getColumnCount(), CellColorModel.class);
        aggregator = new RangeAggregator(model, this);

        model.addTableModelListener(this::tableChanged);
    }

    public void addBorderModes(CellRange range, List<BorderMode> modeList) {
        aggregator.setBorderMode(range, modeList);
    }

    public List<BorderMode> getModes(CellRange range) {
        return aggregator.getModes(range);
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
        CellColorModel model = createColorModelIfNeeded(pointer.getRow(), pointer.getColumn());
        model.setBackground(color);
    }

    private CellColorModel createColorModelIfNeeded(int row, int column) {
        CellColorModel model = getCellColorModel(row, column);
        if (model == null || model == DEFAULT) {
            model = new CellColorModel();
            values.set(row, column, model);
        }
        return model;
    }

    public Color getBackgroundColor(CellRange range) {
        if (range == null) {
            return ProcessorUIDefaults.DEFAULT_BACKGROUND_COLOR;
        }

        Color color = null;
        for (CellPointer pointer : range) {
            CellColorModel model = createColorModelIfNeeded(pointer.getRow(), pointer.getColumn());
            if (color != null && !color.equals(model.getBackground())) {
                return ProcessorUIDefaults.DEFAULT_BACKGROUND_COLOR;
            }
            color = model.getBackground();
        }
        return color;
    }

    public Color getBackgroundColor(CellPointer pointer) {
        CellColorModel model = createColorModelIfNeeded(pointer.getRow(), pointer.getColumn());
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
            CellColorModel model = createColorModelIfNeeded(pointer.getRow(), pointer.getColumn());
            Color lower = model.isNeedLowerLine() ? model.getLowerColor() : null;
            Color righter = model.isNeedRightLine() ? model.getRightColor() : null;
            Color lefter = null;
            Color upper = null;
            if (pointer.getRow() > 0 && pointer.getRow() == range.getFirstRow()) {
                CellColorModel upperModel = createColorModelIfNeeded(pointer.getRow() - 1, pointer.getColumn());
                upper = upperModel.isNeedLowerLine() ? upperModel.getLowerColor() : null;
            }
            if (pointer.getColumn() > 0 && pointer.getColumn() == range.getFirstColumn()) {
                CellColorModel lefterModel = createColorModelIfNeeded(pointer.getRow(), pointer.getColumn() -1);
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
        return getCellColorModel(pointer.getRow(), pointer.getColumn()).isConfigured();
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
        CellColorModel cellColorModel = values.get(pointer.getRow(), pointer.getColumn());
        if (cellColorModel == null) {
            cellColorModel = new CellColorModel();
            values.set(pointer.getRow(), pointer.getColumn(), cellColorModel);
        }
        if (cellColorModel.isNeedLowerLine()) {
            cellColorModel.setLowerColor(color);
        }
    }

    public void setRightLineColor(CellPointer pointer, Color color) {
        CellColorModel cellColorModel = values.get(pointer.getRow(), pointer.getColumn());
        if (cellColorModel == null) {
            cellColorModel = new CellColorModel();
            values.set(pointer.getRow(),pointer.getColumn(), cellColorModel);
        }
        if (cellColorModel.isNeedRightLine()) {
            cellColorModel.setRightColor(color);
        }
    }

    public void setNeedLowerLine(CellPointer pointer, boolean need) {
        CellColorModel cellColorModel = createColorModelIfNeeded(pointer.getRow(), pointer.getColumn());
        cellColorModel.setNeedLowerLine(need);
    }

    public void setNeedRightLine(CellPointer pointer, boolean need) {
        CellColorModel cellColorModel = createColorModelIfNeeded(pointer.getRow(), pointer.getColumn());
        cellColorModel.setNeedRightLine(need);
    }

    public void reset(CellRange range) {
        for (CellPointer pointer : range) {
            CellColorModel model = values.get(pointer.getRow(), pointer.getColumn());
            if (model != null) {
                model.reset();
            }
        }
        BorderMode.DOWN.setModePreferences(this, range, true);
        BorderMode.LEFT.setModePreferences(this, range, true);
    }

    private CellColorModel getCellColorModel(int row, int column) {
        CellColorModel cellColorModel = values.get(row, column);
        return cellColorModel == null ? DEFAULT : cellColorModel;
    }

    private void tableChanged(TableModelEvent event) {
        if (model.getRowCount() > values.rowCount()) {
            values.addRow();
        } else if (model.getColumnCount() > values.columnCount()) {
            values.addColumn();
        }
    }
}
