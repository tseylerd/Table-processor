package util;

import cells.pointer.CellPointer;

/**
 * Expression, which knows, how to move cell pointer
 * @author Dmitriy Tseyler
 */
class PointerMovingExpression {
    private final boolean columnFixed;
    private final boolean rowFixed;
    private final int maxRows;
    private final int maxColumns;
    private final CellPointer pointer;

    PointerMovingExpression(boolean columnFixed, boolean rowFixed, CellPointer pointer, int maxRows, int maxColumns) {
        this.rowFixed = rowFixed;
        this.columnFixed = columnFixed;
        this.pointer = pointer;
        this.maxRows = maxRows;
        this.maxColumns = maxColumns;
    }

    private boolean isRowFixed() {
        return rowFixed;
    }

    private boolean isColumnFixed() {
        return columnFixed;
    }

    private String getRowFixedString() {
        return isRowFixed() ? "$" : "";
    }

    private String getColumnFixedString() {
        return isColumnFixed() ? "$" : "";
    }

    private int filterRowOffset(int offset) {
        boolean outOfRange = pointer.getRow() + offset > maxRows || pointer.getRow() + offset < 0;
        return rowFixed || outOfRange ? 0 : offset;
    }

    private int filterColumnOffset(int offset) {
        boolean outOfRange = pointer.getColumn() + offset >= maxColumns || pointer.getColumn() + offset < 0;
        return columnFixed || outOfRange ? 0 : offset;
    }

    PointerMovingExpression moveAndGet(int rowOffset, int columnOffset) {
        CellPointer moved = CellPointer.getPointer(pointer, filterRowOffset(rowOffset), filterColumnOffset(columnOffset));
        return new PointerMovingExpression(columnFixed, rowFixed, moved, maxRows, maxColumns);
    }

    @Override
    public String toString() {
        return getColumnFixedString() + Util.columnNameByIndex(pointer.getColumn()) + getRowFixedString() + pointer.getRow();
    }
}
