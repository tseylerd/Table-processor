package cells.connection;

import cells.pointer.CellPointer;

/**
 * @author Dmitriy Tseyler
 */
public class PointerNode {
    private boolean visited;
    private final CellPointer pointer;

    public PointerNode(CellPointer pointer) {
        this.pointer = pointer;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PointerNode)) {
            return false;
        }

        return ((PointerNode) obj).pointer.equals(pointer);
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
