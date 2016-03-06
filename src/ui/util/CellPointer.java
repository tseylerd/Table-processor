package ui.util;

/**
 * @author Dmitriy Tseyler
 */
public class CellPointer {
    private final int row;
    private final int column;

    public CellPointer(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
