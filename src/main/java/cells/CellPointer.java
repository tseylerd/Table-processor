package cells;

import ui.table.exceptions.InvalidCellPointerException;
import util.Util;

/**
 * @author Dmitriy Tseyler
 */
public class CellPointer {
    private static final int MULTIPLIER = 31;

    private final int row;
    private final int column;

    public CellPointer(CellPointer pointer, int rowOffset, int columnOffset) {
        this(pointer.row + rowOffset, pointer.column + columnOffset);
    }

    public CellPointer(int row, int column) {
        if (row < 0 || column < 0) {
            throw new InvalidCellPointerException();
        }
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public int hashCode() {
        return row * MULTIPLIER + column;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CellPointer))
            return false;

        CellPointer toCompare = (CellPointer)obj;
        return toCompare.column == column && toCompare.row == row;
    }

    @Override
    public String toString() {
        return Util.columnNameByIndex(column) + (row + 1);
    }
}
