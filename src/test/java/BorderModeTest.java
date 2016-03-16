import cells.CellRange;
import cells.pointer.CellPointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ui.laf.grid.BorderMode;
import ui.laf.grid.TableColorModel;
import ui.table.SpreadSheetModel;
import ui.table.SpreadSheetTable;

import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class BorderModeTest {
    private static final int ROW_COUNT = 100;
    private static final int COLUMN_COUNT = 100;

    private SpreadSheetTable table;
    private SpreadSheetModel model;

    @Before
    public void init() {
        table = new SpreadSheetTable(ROW_COUNT, COLUMN_COUNT);
        model = (SpreadSheetModel) table.getModel();
    }

    @Test
    public void testEnabled() {
        CellRange range = new CellRange(0, 0, 0, 0);
        testEnabled(range, false, true, false, true, false);

        range = new CellRange(0, 0, 1, 1);
        testEnabled(range, true, true, false, true, false);

        range = new CellRange(1, 1, 1, 1);
        testEnabled(range, false, true, true, true, true);

        range = new CellRange(1, 1, 2, 2);
        testEnabled(range, true, true, true, true ,true);

        range = new CellRange(0, 0, ROW_COUNT, COLUMN_COUNT);
        testEnabled(range, true, false, false, false, false);

        range = new CellRange(ROW_COUNT - 1, COLUMN_COUNT - 1, ROW_COUNT, COLUMN_COUNT);
        testEnabled(range, true, false, true, false, true);

        range = new CellRange(ROW_COUNT, COLUMN_COUNT, ROW_COUNT, COLUMN_COUNT);
        testEnabled(range, false, false, true, false, true);
    }

    private void testEnabled(CellRange range, boolean allEnabled, boolean downEnabled, boolean upEnabled,
                             boolean rightEnabled, boolean leftEnabled)
    {
        Assert.assertEquals(BorderMode.ALL_LINES.isModeEnabled(table, range), allEnabled);
        Assert.assertEquals(BorderMode.DOWN.isModeEnabled(table, range), downEnabled);
        Assert.assertEquals(BorderMode.UP.isModeEnabled(table, range), upEnabled);
        Assert.assertEquals(BorderMode.RIGHT.isModeEnabled(table, range), rightEnabled);
        Assert.assertEquals(BorderMode.LEFT.isModeEnabled(table, range), leftEnabled);
    }

    @Test
    public void testGrid() {
        CellRange range = new CellRange(1, 1, ROW_COUNT - 1, COLUMN_COUNT - 1);
        testAllLinesMode(range);
        testDownMode(range);
        testUpMode(range);
        testLeftMode(range);
        testRightMode(range);
    }

    @Test
    public void testColors() {
        CellRange range = new CellRange(1, 1, ROW_COUNT - 1, COLUMN_COUNT - 1);
        TableColorModel colorModel = new TableColorModel(model);
        colorModel.setRangeLineColor(range, Color.BLUE);
        colorModel.setBackgroundColor(range, Color.GRAY);
        for (CellPointer pointer : range) {
            Assert.assertEquals(colorModel.getLowerLineColor(pointer), Color.BLUE);
            Assert.assertEquals(colorModel.getBackgroundColor(pointer), Color.GRAY);
        }
        for (CellPointer pointer : range.getFirstRowRange()) {
            Assert.assertEquals(colorModel.getLowerLineColor(CellPointer.getPointer(pointer, -1, 0)), Color.BLUE);
        }
        for (CellPointer pointer : range.getFirstColumnRange()) {
            Assert.assertEquals(colorModel.getRightLineColor(CellPointer.getPointer(pointer, 0, -1)), Color.BLUE);
        }
    }

    private void testAllLinesMode(CellRange range) {
        TableColorModel tableColorModel = new TableColorModel(model);
        BorderMode.ALL_LINES.setModePreferences(tableColorModel, range, false);
        for (CellPointer pointer : range) {
            Assert.assertFalse(tableColorModel.needLowerLine(pointer) && pointer.getRow() != range.getLastRow());
            Assert.assertFalse(tableColorModel.needRightLine(pointer) && pointer.getColumn() != range.getLastColumn());
        }

        BorderMode.ALL_LINES.setModePreferences(tableColorModel, range, true);
        for (CellPointer pointer : range) {
            Assert.assertTrue(tableColorModel.needLowerLine(pointer) || pointer.getRow() == range.getLastRow());
            Assert.assertTrue(tableColorModel.needRightLine(pointer) || pointer.getColumn() == range.getLastColumn());
        }

        compareToNewGridModel(tableColorModel);
    }

    private void testDownMode(CellRange range) {
        TableColorModel tableColorModel = new TableColorModel(model);
        BorderMode.DOWN.setModePreferences(tableColorModel, range, false);
        for (CellPointer pointer : range.getLastRowRange()) {
            Assert.assertFalse(tableColorModel.needLowerLine(pointer));
        }
        BorderMode.DOWN.setModePreferences(tableColorModel, range, true);
        for (CellPointer pointer : range.getLastRowRange()) {
            Assert.assertTrue(tableColorModel.needLowerLine(pointer));
        }
        compareToNewGridModel(tableColorModel);
    }

    private void testUpMode(CellRange range) {
        TableColorModel tableColorModel = new TableColorModel(model);
        BorderMode.UP.setModePreferences(tableColorModel, range, false);
        for (CellPointer pointer : range.getFirstRowRange()) {
            Assert.assertFalse(tableColorModel.needLowerLine(CellPointer.getPointer(pointer, -1, 0)));
        }
        BorderMode.UP.setModePreferences(tableColorModel, range, true);
        for (CellPointer pointer : range.getFirstRowRange()) {
            Assert.assertTrue(tableColorModel.needLowerLine(CellPointer.getPointer(pointer, -1, 0)));
        }
        compareToNewGridModel(tableColorModel);
    }

    private void testRightMode(CellRange range) {
        TableColorModel tableColorModel = new TableColorModel(model);
        BorderMode.RIGHT.setModePreferences(tableColorModel, range, false);
        for (CellPointer pointer : range.getLastColumnRange()) {
            Assert.assertFalse(tableColorModel.needRightLine(pointer));
        }
        BorderMode.RIGHT.setModePreferences(tableColorModel, range, true);
        for (CellPointer pointer : range.getLastColumnRange()) {
            Assert.assertTrue(tableColorModel.needRightLine(pointer));
        }
        compareToNewGridModel(tableColorModel);
    }


    private void testLeftMode(CellRange range) {
        TableColorModel tableColorModel = new TableColorModel(model);
        BorderMode.LEFT.setModePreferences(tableColorModel, range, false);
        for (CellPointer pointer : range.getFirstColumnRange()) {
            Assert.assertFalse(tableColorModel.needRightLine(CellPointer.getPointer(pointer, 0, -1)));
        }
        BorderMode.LEFT.setModePreferences(tableColorModel, range, true);
        for (CellPointer pointer : range.getFirstColumnRange()) {
            Assert.assertTrue(tableColorModel.needRightLine(CellPointer.getPointer(pointer, 0, -1)));
        }
        compareToNewGridModel(tableColorModel);
    }

    private void compareToNewGridModel(TableColorModel tableColorModel) {
        CellRange range = new CellRange(0, 0, ROW_COUNT - 1, COLUMN_COUNT - 1);
        TableColorModel newModel = new TableColorModel(model);
        for (CellPointer pointer : range) {
            Assert.assertEquals(tableColorModel.needLowerLine(pointer), newModel.needLowerLine(pointer));
            Assert.assertEquals(tableColorModel.needRightLine(pointer), newModel.needRightLine(pointer));
            Assert.assertEquals(tableColorModel.getRightLineColor(pointer), newModel.getRightLineColor(pointer));
            Assert.assertEquals(tableColorModel.getLowerLineColor(pointer), newModel.getLowerLineColor(pointer));
        }
    }
}
