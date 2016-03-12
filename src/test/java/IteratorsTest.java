import cells.pointer.CellPointer;
import cells.CellRange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 * @author Dmitriy Tseyler
 */
public class IteratorsTest {
    private static final int START_ROW = 0;
    private static final int START_COLUMN = 0;
    private static final int END_COLUMN = 100;
    private static final int END_ROW = 100;

    private CellRange range;

    @Before
    public void init() {
        range = new CellRange(START_ROW, START_COLUMN, END_ROW, END_COLUMN);
    }

    @Test
    public void testCellColumnRowIterator() {
        testCellIterator(START_ROW - 1, START_COLUMN - 1, END_ROW, END_COLUMN,
                (pointer, row, column) -> pointer.getColumn() == column + 1 || pointer.getColumn() == START_COLUMN && pointer.getRow() == row + 1,
                range::iterator);
    }

    @Test
    public void testCellRowColumnIterator() {
        testCellIterator(START_ROW - 1, START_COLUMN - 1, END_ROW, END_COLUMN,
                (pointer, row, column) -> pointer.getRow() == row + 1 || pointer.getRow() == START_ROW && pointer.getColumn() == column + 1,
                range::rowColumnIterator);
    }

    @Test
    public void testCellInverseColumnRowIterator() {
        testCellIterator(END_ROW + 1, END_COLUMN + 1, START_ROW, START_COLUMN,
                (pointer, row, column) -> pointer.getColumn() == column - 1 || pointer.getColumn() == END_COLUMN && pointer.getRow() == row - 1,
                range::inverseColumnRowIterator);
    }

    @Test
    public void testCellInverseRowColumnIterator() {
        testCellIterator(END_ROW + 1, END_COLUMN + 1, START_ROW, START_COLUMN,
                (pointer, row, column) -> pointer.getRow() == row - 1 || pointer.getRow() == END_ROW && pointer.getColumn() == column - 1,
                range::inverseRowColumnIterator);
    }

    private void testCellIterator(int firstRow, int firstColumn, int lastRow, int lastColumn, TripleCellPredicate predicate,
                                  Supplier<Iterator<CellPointer>> supplier)
    {
        int row = firstRow;
        int column = firstColumn;
        Iterator<CellPointer> iterator = supplier.get();
        Assert.assertTrue(iterator.hasNext());
        while (iterator.hasNext()) {
            CellPointer pointer = iterator.next();
            Assert.assertTrue(predicate.test(pointer, row, column) || pointer.getRow() == lastRow && pointer.getColumn() == lastColumn);
            row = pointer.getRow();
            column = pointer.getColumn();
        }
    }

    @Test
    public void testRowRangeIterator() {
        Iterator<CellRange> iterator = range.rangeIterator();
        int start = START_ROW - 1;
        while (iterator.hasNext()) {
            CellRange range = iterator.next();
            Assert.assertEquals(range.getFirstRow(), start + 1);
            Assert.assertEquals(range.getFirstColumn(), START_COLUMN);
            Assert.assertEquals(range.getLastColumn(), END_COLUMN);
            start = range.getFirstRow();
        }
    }

    @Test
    public void testInverseRowRangeIterator() {
        Iterator<CellRange> iterator = range.inverseRangeIterator();
        int start = END_ROW + 1;
        while (iterator.hasNext()) {
            CellRange range = iterator.next();
            Assert.assertEquals(range.getFirstRow(), start - 1);
            Assert.assertEquals(range.getFirstColumn(), START_COLUMN);
            Assert.assertEquals(range.getLastColumn(), END_COLUMN);
            start = range.getFirstRow();
        }
    }

    @Test
    public void testColumnRangeIterator() {
        Iterator<CellRange> iterator = range.columnRangeIterator();
        int start = START_COLUMN - 1;
        while (iterator.hasNext()) {
            CellRange range = iterator.next();
            Assert.assertEquals(range.getFirstColumn(), start + 1);
            Assert.assertEquals(range.getFirstRow(), START_ROW);
            Assert.assertEquals(range.getLastRow(), END_ROW);
            start = range.getFirstColumn();
        }
    }

    @Test
    public void testInverseColumnRangeIterator() {
        Iterator<CellRange> iterator = range.inverseColumnRangeIterator();
        int start = END_COLUMN + 1;
        while (iterator.hasNext()) {
            CellRange range = iterator.next();
            Assert.assertEquals(range.getFirstColumn(), start - 1);
            Assert.assertEquals(range.getFirstRow(), START_ROW);
            Assert.assertEquals(range.getLastRow(), END_ROW);
            start = range.getFirstColumn();
        }
    }

    private interface TripleCellPredicate {
        boolean test(CellPointer pointer, int row, int column);
    }
}
