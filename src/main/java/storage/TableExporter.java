package storage;

import cells.CellRange;
import cells.CellValue;
import cells.pointer.CellPointer;
import ui.laf.grid.TableColorModel;
import ui.table.SpreadSheetModel;
import ui.table.SpreadSheetTable;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Used for export table to file
 * @author Dmitriy Tseyler
 */
public class TableExporter {
    public static final char CELL_VALUE_BEGIN_TAG = '{';
    public static final char CELL_VALUE_END_TAG = '}';
    public static final char VALUE_END_TAG = '>';
    public static final char VALUE_BEGIN_TAG = '<';
    public static final char COLOR_MODEL_BEGIN = '$';

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
            if (!model.isConfigured(pointer)) {
                continue;
            }
            CellValue value = model.getValueAt(pointer);
            writer.append(CELL_VALUE_BEGIN_TAG);
            append(pointer.getRow());
            append(pointer.getColumn());
            append(value.getEditorValue());
            writer.append(CELL_VALUE_END_TAG);
            writer.append("\n");
        }
        writer.append(COLOR_MODEL_BEGIN);
        writer.append("\n");
        Map<CellRange, Color> rightColorsMap = colorModel.getRightLineColorMap();
        Map<CellRange, Color> bottomColorsMap = colorModel.getBottomLineColorMap();
        Map<CellRange, Color> backgroundColorsMap = colorModel.getBackgroundMap();
        appendColorMap(rightColorsMap);
        appendColorMap(bottomColorsMap);
        appendColorMap(backgroundColorsMap);

        Map<CellRange, Boolean> rightLineNeedMap = colorModel.getRightLineMap();
        Map<CellRange, Boolean> bottomLineNeedMap = colorModel.getBottomLineMap();
        appendBoolMap(rightLineNeedMap);
        appendBoolMap(bottomLineNeedMap);

        writer.flush();
        writer.close();
    }

    private void appendColorMap(Map<CellRange, Color> colorMap) throws IOException {
        append(colorMap.size());
        writer.append("\n");
        for (Map.Entry<CellRange, Color> cellRangeColorEntry : colorMap.entrySet()) {
            CellPointer begin = cellRangeColorEntry.getKey().getBegin();
            CellPointer end = cellRangeColorEntry.getKey().getEnd();
            append(begin.getRow());
            append(begin.getColumn());
            append(end.getRow());
            append(end.getColumn());
            append(cellRangeColorEntry.getValue().getRGB());
        }
        writer.append("\n");
    }

    private void appendBoolMap(Map<CellRange, Boolean> boolMap) throws IOException {
        append(boolMap.size());
        writer.append("\n");
        for (Map.Entry<CellRange, Boolean> cellRangeBooleanEntry : boolMap.entrySet()) {
            CellPointer begin = cellRangeBooleanEntry.getKey().getBegin();
            CellPointer end = cellRangeBooleanEntry.getKey().getEnd();
            append(begin.getRow());
            append(begin.getColumn());
            append(end.getRow());
            append(end.getColumn());
            append(cellRangeBooleanEntry.getValue());
        }
        writer.append("\n");
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
