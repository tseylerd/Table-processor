package main.java.cells;

import java.util.Iterator;

/**
 * @author Dmitriy Tseyler
 */
public class CellIterator implements Iterator<CellPointer> {
    private final CellRange range;
    private CellPointer current;

    CellIterator(CellRange range) {
        this.range = range;
        current = range.getBegin();
    }

    @Override
    public CellPointer next() {
        CellPointer result = current;
        current = nextOf(current);
        return result;
    }

    private CellPointer nextOf(CellPointer previous) {
        int row = previous.getRow();
        int column = previous.getColumn();
        if (row < range.getEnd().getRow()) {
            row++;
        } else if (column < range.getEnd().getColumn()){
            row = range.getBegin().getRow();
            column++;
        } else {
            return null;
        }

        return new CellPointer(row, column);
    }

    @Override
    public boolean hasNext() {
        return current != null && (current.getColumn() <= range.getEnd().getColumn() || current.getRow() <= range.getEnd().getRow());
    }
}
