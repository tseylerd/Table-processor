import cells.CellRange;
import cells.CellValue;
import cells.pointer.CellPointer;
import org.junit.Assert;
import org.junit.Test;
import storage.ImportFormatException;
import storage.TableExporter;
import storage.TableImporter;
import ui.laf.ProcessorUIDefaults;
import ui.laf.grid.TableColorModel;
import ui.table.SpreadSheetModel;
import ui.table.SpreadSheetTable;

import java.awt.*;
import java.io.*;
import java.util.Random;

/**
 * @author Dmitriy Tseyler
 */
public class ImportExportTest {

    @Test
    public void test() {
        SpreadSheetTable table = new SpreadSheetTable();
        TableColorModel colorModel = table.getTableColorModel();
        SpreadSheetModel spreadSheetModel = (SpreadSheetModel) table.getModel();
        CellRange range = new CellRange(0, 0, ProcessorUIDefaults.DEFAULT_ROW_COUNT - 1, ProcessorUIDefaults.DEFAULT_COLUMN_COUNT - 1);
        for (int i = 0; i < 100; i++) {
            setRandomSettings(colorModel, spreadSheetModel);
        }

        StringWriter writer = new StringWriter();
        try {
            new TableExporter(table, new BufferedWriter(writer)).export();
        } catch (IOException e) {
            Assert.assertTrue(false);
        }

        StringReader reader = new StringReader(writer.toString());
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            SpreadSheetTable imported = new TableImporter(bufferedReader).importTable();
            TableColorModel importedColorModel = imported.getTableColorModel();
            SpreadSheetModel importedSpreadSheetModel = (SpreadSheetModel)imported.getModel();
            for (CellPointer pointer : range) {
                Assert.assertEquals(colorModel.getBackgroundColor(pointer), importedColorModel.getBackgroundColor(pointer));
                Assert.assertEquals(colorModel.getBottomLineColor(pointer), importedColorModel.getBottomLineColor(pointer));
                Assert.assertEquals(colorModel.getRightLineColor(pointer), importedColorModel.getRightLineColor(pointer));
                Assert.assertEquals(colorModel.needBottomLine(pointer), importedColorModel.needBottomLine(pointer));
                Assert.assertEquals(colorModel.needRightLine(pointer), importedColorModel.needBottomLine(pointer));
                Assert.assertEquals(spreadSheetModel.getValueAt(pointer), importedSpreadSheetModel.getValueAt(pointer));
            }
        } catch (IOException | ImportFormatException e) {
            Assert.assertTrue(false);
        }
    }

    private void setRandomSettings(TableColorModel model, SpreadSheetModel spreadSheetModel) {
        Random r = new Random();
        int row = r.nextInt(20);
        int column = r.nextInt(20);
        int endRow = r.nextInt(20) + 20;
        int endColumn = r.nextInt(20) + 20;
        CellRange range = new CellRange(row, column, endRow, endColumn);
        int rgb = r.nextInt(255);
        Color color = new Color(rgb);
        int bool = r.nextInt(2);
        boolean needLine = bool == 1;
        int value = r.nextInt(100);
        model.setBackgroundColor(range, color);
        model.setGridColor(range, color);
        model.setNeedRightLine(range, needLine);
        model.setNeedBottomLine(range, needLine);
        for (CellPointer pointer : range) {
            spreadSheetModel.setValueAt(new CellValue(String.valueOf(value)), pointer);
        }
    }
}
