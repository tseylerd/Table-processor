package cells;

/**
 * Result of range splitting by another range.
 * @author Dmitriy Tseyler
 */
class SplittedRange {
    private final CellRange left;
    private final CellRange right;
    private final CellRange down;
    private final CellRange up;
    private final CellRange splitter;

    SplittedRange(CellRange up, CellRange down, CellRange left, CellRange right, CellRange splitter) {
        this.left = left;
        this.right = right;
        this.down = down;
        this.up = up;
        this.splitter = splitter;
    }

    CellRange getDown() {
        return down;
    }

    CellRange getLeft() {
        return left;
    }

    CellRange getUp() {
        return up;
    }

    CellRange getRight() {
        return right;
    }

    CellRange getSplitter() {
        return splitter;
    }

    boolean splitSuccessful() {
        return up != null
                || down != null
                || right != null
                || left  != null;
    }
}
