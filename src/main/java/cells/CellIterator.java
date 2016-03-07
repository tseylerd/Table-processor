package cells;

/**
 * @author Dmitriy Tseyler
 */
public class CellIterator extends AbstractCellIterator {

    public CellIterator(CellRange range) {
        super(range);
    }

    @Override
    protected CellPointer getBegin() {
        return range.getBegin();
    }

    @Override
    protected int getOffset() {
        return 1;
    }

    @Override
    protected boolean needChangeColumn(int column) {
        return column < range.getEnd().getColumn();
    }

    @Override
    protected boolean needChangeRow(int row) {
        return row < range.getEnd().getRow();
    }
}
