package ui.laf.grid;

import cells.CellRange;
import ui.table.SpreadSheetModel;

/**
 * @author Dmitriy Tseyler
 */
public enum BorderMode {
    LEFT("Left border") {
        @Override
        public void setModePreferences(TableColorModel model, CellRange range, boolean on) {
            CellRange firstColumnRange = range.getFirstColumnRange();
            if (firstColumnRange.getFirstColumn() > 0) {
                model.setNeedRightLine(new CellRange(range.getFirstRow(), range.getFirstColumn() - 1, range.getLastRow(), range.getFirstColumn() - 1), on);
            }
        }

        @Override
        boolean turnedOn(TableColorModel model, CellRange range) {
            if (range.getFirstColumn() == 0)
                return true;

            return model.getNeedRightLine(new CellRange(range.getFirstRow(), range.getFirstColumn() - 1, range.getLastRow(), range.getFirstColumn() - 1));
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
            model.setNeedRightLine(lastColumnRange, on);
        }

        @Override
        boolean turnedOn(TableColorModel model, CellRange range) {
            return model.getNeedRightLine(range.getLastColumnRange());
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
            model.setNeedBottomLine(lastRowRange, on);
        }

        @Override
        boolean turnedOn(TableColorModel model, CellRange range) {
            return model.getNeedBottomLine(range.getLastRowRange());
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
                model.setNeedBottomLine(new CellRange(range.getFirstRow() - 1, range.getFirstColumn(), range.getFirstRow() - 1, range.getLastColumn()), on);
            }
        }

        @Override
        boolean turnedOn(TableColorModel model, CellRange range) {
            if (range.getFirstRow() == 0)
                return true;

            return model.getNeedBottomLine(new CellRange(range.getFirstRow() - 1, range.getFirstColumn(), range.getFirstRow() - 1, range.getLastColumn()));
        }

        @Override
        boolean enabled(SpreadSheetModel model, CellRange range) {
            return range.getFirstRow() > 0;
        }
    },
    ALL_LINES("Grid") {
        @Override
        public void setModePreferences(TableColorModel model, CellRange range, boolean on) {
            CellRange cutted = new CellRange(range.getFirstRow(), range.getFirstColumn(), range.getLastRow() - 1, range.getLastColumn() - 1);
            model.setNeedBottomLine(cutted, on);
            model.setNeedRightLine(cutted, on);
            model.setNeedRightLine(new CellRange(range.getLastRow(), range.getFirstColumn(), range.getLastRow(), range.getLastColumn() - 1), on);
            model.setNeedBottomLine(new CellRange(range.getFirstRow(), range.getLastColumn(), range.getLastRow() - 1, range.getLastColumn()), on);
        }

        @Override
        boolean turnedOn(TableColorModel model, CellRange range) {
            boolean lower = true;
            boolean right = true;
            CellRange cutted = range;
            if (cutted.getFirstRow() < cutted.getLastRow()) {
                cutted = new CellRange(cutted.getFirstRow(), cutted.getFirstColumn(), cutted.getLastRow() - 1, cutted.getLastColumn());
            }
            if (cutted.getFirstColumn() < cutted.getLastColumn()) {
                cutted = new CellRange(cutted.getFirstRow(), cutted.getFirstColumn(), cutted.getLastRow(), cutted.getLastColumn() - 1);
            }
            if (cutted.isValid()) {
                lower = model.getNeedBottomLine(cutted);
                right = model.getNeedRightLine(cutted);
            }
            CellRange lastRow = new CellRange(range.getLastRow(), range.getFirstColumn(), range.getLastRow(), range.getLastColumn() - 1);
            if (lastRow.isValid()) {
                right &= model.getNeedRightLine(lastRow);
            }
            CellRange lastColumn = new CellRange(range.getFirstRow(), range.getLastColumn(), range.getLastRow() - 1, range.getLastColumn());
            if (lastRow.isValid()) {
                lower &= model.getNeedBottomLine(lastColumn);
            }
            return right & lower;
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
