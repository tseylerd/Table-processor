import cells.CellRange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author Dmitriy Tseyler
 */
public class CellRangeTest {
    private static final int ROWS = 40;
    private static final int COLUMNS = 40;
    private static final int START = 10;
    private CellRange range;

    @Before
    public void init() {
        range = new CellRange(START, START, ROWS, COLUMNS);
    }

    @Test
    public void testLefterUpperCatching() {
        CellRange test = new CellRange(0, 0, 20, 20);
        List<CellRange> ranges = range.split(test);
        Assert.assertEquals(ranges.size(), 2);

        Assert.assertEquals(ranges.get(0), new CellRange(21, START, ROWS, COLUMNS)); // bottom
        Assert.assertEquals(ranges.get(1), new CellRange(START, 21, ROWS, COLUMNS)); // right
    }

    @Test
    public void testUpperCatching() {
        CellRange test = new CellRange(0, START, 20, COLUMNS);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 1);
        Assert.assertEquals(ranges.get(0), new CellRange(21, START, ROWS, COLUMNS)); // down
    }

    @Test
    public void testRighterUpperCatching() {
        CellRange test = new CellRange(0, 20, 20, 50);
        List<CellRange> ranges = range.split(test);
        Assert.assertEquals(ranges.size(), 2);

        Assert.assertEquals(ranges.get(1), new CellRange(START, START, ROWS, 19)); // left
        Assert.assertEquals(ranges.get(0), new CellRange(21, START, ROWS, COLUMNS)); // down
    }

    @Test
    public void testRighterCatching() {
        CellRange test = new CellRange(0, 20, 50, 50);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 1);
        Assert.assertEquals(ranges.get(0), new CellRange(START, START, ROWS, 19)); // left
    }

    @Test
    public void testBottomRightCatching() {
        CellRange test = new CellRange(20, 20, 50, 50);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 2);
        Assert.assertEquals(ranges.get(0), new CellRange(START, START, 19, COLUMNS)); // upper
        Assert.assertEquals(ranges.get(1), new CellRange(START, START, ROWS, 19)); // lefter
    }

    @Test
    public void testRightCatching() {
        CellRange test = new CellRange(10, 20, 40, 50);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 1);
        Assert.assertEquals(ranges.get(0), new CellRange(START, START, ROWS, 19)); // lefter
    }

    @Test
    public void testBottomSplitting() {
        CellRange test = new CellRange(20, START, 50, COLUMNS);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 1);
        Assert.assertEquals(ranges.get(0), new CellRange(START, START, 19, COLUMNS)); // upper
    }

    @Test
    public void testBottomLefterSplitting() {
        CellRange test = new CellRange(20, 0, 50, 20);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 2);
        Assert.assertEquals(ranges.get(0), new CellRange(START, START, 19, COLUMNS)); // upper
        Assert.assertEquals(ranges.get(1), new CellRange(START, 21, ROWS, COLUMNS)); // righter
    }


    @Test
    public void testLefterSplitting() {
        CellRange test = new CellRange(START, 0, ROWS, 20);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 1);
        Assert.assertEquals(ranges.get(0), new CellRange(START, 21, ROWS, COLUMNS)); // righter
    }

    @Test
    public void testUpInsideSplitting() {
        CellRange test = new CellRange(START, 20, 20, 30);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 3);
        Assert.assertEquals(ranges.get(0), new CellRange(21, START, ROWS, COLUMNS)); // bottom
        Assert.assertEquals(ranges.get(1), new CellRange(START, START, ROWS, 19)); // lefter
        Assert.assertEquals(ranges.get(2), new CellRange(START, 31, ROWS, COLUMNS)); // righter
    }

    @Test
    public void testDownInsideSplitting() {
        CellRange test = new CellRange(20, 20, ROWS, 30);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 3);
        Assert.assertEquals(ranges.get(0), new CellRange(START, START, 19, COLUMNS)); // upper
        Assert.assertEquals(ranges.get(1), new CellRange(START, START, ROWS, 19)); // lefter
        Assert.assertEquals(ranges.get(2), new CellRange(START, 31, ROWS, COLUMNS)); // righter
    }

    @Test
    public void testLeftInsideSplitting() {
        CellRange test = new CellRange(20, START, 30, 30);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 3);
        Assert.assertEquals(ranges.get(0), new CellRange(START, START, 19, COLUMNS)); // upper
        Assert.assertEquals(ranges.get(1), new CellRange(31, START, ROWS, COLUMNS)); // bottom
        Assert.assertEquals(ranges.get(2), new CellRange(START, 31, ROWS, COLUMNS)); // righter
    }

    @Test
    public void testRightInsideSplitting() {
        CellRange test = new CellRange(20, 20, 30, COLUMNS);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 3);
        Assert.assertEquals(ranges.get(0), new CellRange(START, START, 19, COLUMNS)); // upper
        Assert.assertEquals(ranges.get(1), new CellRange(31, START, ROWS, COLUMNS)); // bottom
        Assert.assertEquals(ranges.get(2), new CellRange(START, START, ROWS, 19)); // lefter
    }

    @Test
    public void testInsideSplitting() {
        CellRange test = new CellRange(20, 20, 30, 30);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 4);
        Assert.assertEquals(ranges.get(0), new CellRange(START, START, 19, COLUMNS)); // upper
        Assert.assertEquals(ranges.get(1), new CellRange(31, START, ROWS, COLUMNS)); // bottom
        Assert.assertEquals(ranges.get(2), new CellRange(START, START, ROWS, 19)); // lefter
        Assert.assertEquals(ranges.get(3), new CellRange(START, 31, ROWS, COLUMNS)); // righter
    }

    @Test
    public void testNoSplitting() {
        CellRange test = new CellRange(0, 0, 50, 50);
        List<CellRange> ranges = range.split(test);

        Assert.assertEquals(ranges.size(), 0);
    }


    @Test
    public void testNoSplitting2() {
        CellRange test = new CellRange(100, 100, 110, 110);
        List<CellRange> ranges = range.split(test);

        Assert.assertNull(ranges);
    }
}
