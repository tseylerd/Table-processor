package storage;

import cells.CellRange;
import cells.CellValue;
import cells.pointer.CellPointer;
import ui.laf.grid.TableColorModel;
import ui.table.SpreadSheetModel;
import ui.table.SpreadSheetTable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Dmitriy Tseyler
 */
public class TableExporter {
    public static final char CELL_VALUE_BEGIN_TAG = '{';
    public static final char CELL_VALUE_END_TAG = '}';
    public static final char VALUE_END_TAG = '>';
    public static final char VALUE_BEGIN_TAG = '<';

    private final SpreadSheetTable table;
    private final BufferedWriter writer;

    public TableExporter(SpreadSheetTable table, BufferedWriter writer) {
        this.table = table;
        this.writer = writer;
    }

    public void export() throws IOException {
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        CellRange range = new CellRange(0, 0, rowCount - 1, columnCount - 1);
        SpreadSheetModel model = (SpreadSheetModel)table.getModel();
        TableColorModel colorModel = table.getTableColorModel();
        append(rowCount);
        append(columnCount);
        writer.append('\n');
        for (CellPointer pointer : range) {
            if (!model.isConfigured(pointer) && !colorModel.isConfigured(pointer)) {
                continue;
            }
            CellValue value = model.getValueAt(pointer);
            writer.append(CELL_VALUE_BEGIN_TAG);
            append(pointer.getRow());
            append(pointer.getColumn());
            append(value.getEditorValue());
            append(colorModel.getLowerLineColor(pointer).getRGB());
            append(colorModel.getRightLineColor(pointer).getRGB());
            append(colorModel.getBackgroundColor(pointer).getRGB());
            append(colorModel.needLowerLine(pointer));
            append(colorModel.needRightLine(pointer));
            writer.append(CELL_VALUE_END_TAG);
            writer.append("\n");
        }
        writer.flush();
        writer.close();
    }

    private void append(boolean value) throws IOException {
        append(String.valueOf(value));
    }

    private void append(int value) throws IOException {
        append(String.valueOf(value));
    }

    private void append(String value) throws IOException {
        writer.append(String.valueOf(value.length()));
        writer.append(VALUE_BEGIN_TAG);
        writer.append(value);
        writer.append(VALUE_END_TAG);
    }
}
