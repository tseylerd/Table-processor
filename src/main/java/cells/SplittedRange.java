package cells;

/**
 * @author Dmitriy Tseyler
 */
public class SplittedRange {
    private final CellRange left;
    private final CellRange right;
    private final CellRange down;
    private final CellRange up;
    private final CellRange splitter;

    public SplittedRange(CellRange up, CellRange down, CellRange left, CellRange right, CellRange splitter) {
        this.left = left;
        this.right = right;
        this.down = down;
        this.up = up;
        this.splitter = splitter;
    }

    public CellRange getDown() {
        return down;
    }

    public CellRange getLeft() {
        return left;
    }

    public CellRange getUp() {
        return up;
    }

    public CellRange getRight() {
        return right;
    }

    public CellRange getSplitter() {
        return splitter;
    }

    public boolean splitSucceful() {
        return up != null
                || down != null
                || right != null
                || left  != null;
    }
}
