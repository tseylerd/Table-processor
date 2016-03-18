package cells.connection;

import cells.pointer.CellPointer;

/**
 * Node of cells graph
 * @author Dmitriy Tseyler
 */
public class PointerNode {
    private boolean visited;
    private final CellPointer pointer;

    public PointerNode(int row, int column) {
        this(CellPointer.getPointer(row, column));
    }

    public PointerNode(CellPointer pointer) {
        this.pointer = pointer;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PointerNode
                && ((PointerNode) obj).pointer.equals(pointer);
    }

    public int getRow() {
        return pointer.getRow();
    }

    public int getColumn() {
        return pointer.getColumn();
    }

    @Override
    public int hashCode() {
        return pointer.hashCode();
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    public CellPointer getPointer() {
        return pointer;
    }
}
