package cells.pointer;

/**
 * @author Dmitriy Tseyler
 */
class CellPointerPool {
    private static final CellPointer[][] pointers = new CellPointer[Byte.MAX_VALUE][Byte.MAX_VALUE];

    static CellPointer getPointer(int row, int column) {
        if (row >= Byte.MAX_VALUE || column >= Byte.MAX_VALUE) {
            return null;
        }
        return pointers[row][column];
    }

    static CellPointer getPointer(CellPointer pointer, int rowOffset, int columnOffset) {
        return getPointer(pointer.getRow() + rowOffset, pointer.getColumn() + columnOffset);
    }

    static void tryCache(CellPointer pointer) {
        if (pointer.getRow() > Byte.MAX_VALUE || pointer.getColumn() > Byte.MAX_VALUE)
            return;

        if (pointers[pointer.getRow()][pointer.getColumn()] != null)
            throw new IllegalStateException("This should never happen");

        pointers[pointer.getRow()][pointer.getColumn()] = pointer;
    }
}
