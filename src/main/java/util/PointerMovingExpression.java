package util;

import cells.pointer.CellPointer;
import ui.table.exceptions.InvalidCellPointerException;

/**
 * @author Dmitriy Tseyler
 */
public class PointerMovingExpression {
    private final boolean columnFixed;
    private final boolean rowFixed;
    private final CellPointer pointer;

    public PointerMovingExpression(boolean columnFixed, boolean rowFixed, CellPointer pointer) {
        this.rowFixed = rowFixed;
        this.columnFixed = columnFixed;
        this.pointer = pointer;
    }

    public boolean isRowFixed() {
        return rowFixed;
    }

    public boolean isColumnFixed() {
        return columnFixed;
    }

    public CellPointer getPointer() {
        return pointer;
    }

    private String getRowFixedString() {
        return isRowFixed() ? "$" : "";
    }

    private String getColumnFixedString() {
        return isColumnFixed() ? "$" : "";
    }

    private int filterRowOffset(int offset) {
        return rowFixed ? 0 : offset;
    }

    private int filterColumnOffset(int offset) {
        return columnFixed ? 0 : offset;
    }

    public PointerMovingExpression moveAndGet(int rowOffset, int columnOffset) throws InvalidCellPointerException {
        CellPointer moved = CellPointer.getPointer(pointer, filterRowOffset(rowOffset), filterColumnOffset(columnOffset));
        return new PointerMovingExpression(columnFixed, rowFixed, moved);
    }

    @Override
    public String toString() {
        return getColumnFixedString() + Util.columnNameByIndex(pointer.getColumn()) + getRowFixedString() + pointer.getRow();
    }
}
