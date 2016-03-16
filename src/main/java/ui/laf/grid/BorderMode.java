package ui.laf.grid;

import cells.CellRange;
import cells.pointer.CellPointer;
import ui.table.SpreadSheetModel;
import ui.table.SpreadSheetTable;

/**
 * @author Dmitriy Tseyler
 */
public enum BorderMode {
    LEFT("Left border") {
        @Override
        public void setModePreferences(TableColorModel model, CellRange range, boolean on) {
            CellRange firstColumnRange = range.getFirstColumnRange();
            if (firstColumnRange.getFirstColumn() > 0) {
                for (CellPointer cellPointer : firstColumnRange) {
                    model.setNeedRightLine(CellPointer.getPointer(cellPointer, 0, -1), on);
                }
            }
        }

        @Override
        boolean turnedOn(TableColorModel model, CellRange range) {
            if (range.getFirstColumn() == 0)
                return true;

            for (CellPointer pointer : range.getFirstColumnRange()) {
                CellPointer lefter = CellPointer.getPointer(pointer, 0, -1);
                if (!model.needRightLine(lefter))
                    return false;
            }
            return true;
        }

        @Override
        boolean enabled(SpreadSheetModel model, CellRange range) {
            return range.getFirstColumn() > 0;
        }
    },
    RIGHT("Right border") {
        @Override
        public void setModePreferences(TableColorModel model, CellRange range, boolean on) {
            CellRange lastColumnRange = range.getLastColumnRange();
            for (CellPointer cellPointer : lastColumnRange) {
                    model.setNeedRightLine(cellPointer, on);
            }
        }

        @Override
        boolean turnedOn(TableColorModel model, CellRange range) {
            for (CellPointer pointer : range.getLastColumnRange()) {
                if (!model.needRightLine(pointer))
                    return false;
            }
            return true;
        }

        @Override
        boolean enabled(SpreadSheetModel model, CellRange range) {
            return range.getLastColumn() < model.getColumnCount();
        }
    },
    DOWN("Lower border") {
        @Override
        public void setModePreferences(TableColorModel model, CellRange range, boolean on) {
            CellRange lastRowRange = range.getLastRowRange();
            for (CellPointer cellPointer : lastRowRange) {
                model.setNeedLowerLine(cellPointer, on);
            }
        }

        @Override
        boolean turnedOn(TableColorModel model, CellRange range) {
            for (CellPointer pointer : range.getLastRowRange()) {
                if (!model.needLowerLine(pointer))
                    return false;
            }
            return true;
        }

        @Override
        boolean enabled(SpreadSheetModel model, CellRange range) {
            return range.getLastRow() < model.getRowCount();
        }
    },
    UP("Upper border") {
        @Override
        public void setModePreferences(TableColorModel model, CellRange range, boolean on) {
            CellRange firstRowRange = range.getFirstRowRange();
            if (firstRowRange.getFirstRow() > 0) {
                for (CellPointer cellPointer : firstRowRange) {
                    model.setNeedLowerLine(CellPointer.getPointer(cellPointer, -1, 0), on);
                }
            }
        }

        @Override
        boolean turnedOn(TableColorModel model, CellRange range) {
            if (range.getFirstRow() == 0)
                return true;

            for (CellPointer pointer : range.getFirstRowRange()) {
                CellPointer lefter = CellPointer.getPointer(pointer, -1, 0);
                if (!model.needLowerLine(lefter))
                    return false;
            }
            return true;
        }

        @Override
        boolean enabled(SpreadSheetModel model, CellRange range) {
            return range.getFirstRow() > 0;
        }
    },
    ALL_LINES("Grid") {
        @Override
        public void setModePreferences(TableColorModel model, CellRange range, boolean on) {
            for (CellPointer pointer : range) {
                if (pointer.getRow() < range.getLastRow()) {
                    model.setNeedLowerLine(pointer, on);
                }
                if (pointer.getColumn() < range.getLastColumn()) {
                    model.setNeedRightLine(pointer, on);
                }
            }
        }

        @Override
        boolean turnedOn(TableColorModel model, CellRange range) {
            for (CellPointer pointer : range) {
                if (!model.needLowerLine(pointer) && pointer.getRow() < range.getLastRow()) {
                    return false;
                }
                if (!model.needRightLine(pointer) && pointer.getColumn() < range.getLastColumn()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        boolean enabled(SpreadSheetModel model, CellRange range) {
            return range.size() > 1;
        }
    };

    private final String guiName;

    BorderMode(String guiName) {
        this.guiName = guiName;
    }

    public String getGuiName() {
        return guiName;
    }

    public boolean isModeEnabled(SpreadSheetModel table, CellRange range) {
        return range != null && enabled(table, range);
    }

    public boolean isModeTurnedOn(TableColorModel model, CellRange range) {
        return range != null && turnedOn(model, range);
    }

    public abstract void setModePreferences(TableColorModel model, CellRange range, boolean on);
    abstract boolean turnedOn(TableColorModel model, CellRange range);
    abstract boolean enabled(SpreadSheetModel model, CellRange range);
}
