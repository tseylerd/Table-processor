package storage;

import cells.CellRange;
import cells.CellValue;
import cells.pointer.CellPointer;
import ui.laf.grid.TableColorModel;
import ui.table.SpreadSheetModel;
import ui.table.SpreadSheetTable;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to import table from file
 * @author Dmitriy Tseyler
 */
public class TableImporter {
    private final BufferedReader reader;

    private String line;
    private int current;

    public TableImporter(BufferedReader reader) {
        this.reader = reader;
        current = 0;
    }

    public SpreadSheetTable importTable() throws IOException, ImportFormatException {
        line = reader.readLine();
        int rowCount = readInt();
        int columnCount = readInt();
        SpreadSheetModel model = new SpreadSheetModel(rowCount, columnCount);
        TableColorModel colorModel = new TableColorModel();
        while ((line = reader.readLine()) != null && !(line.charAt(0) == TableExporter.COLOR_MODEL_BEGIN)) {
            current = 0;
            assertCellBegin();
            int row = readInt();
            int column = readInt();
            CellPointer pointer = CellPointer.getPointer(row, column);
            String editorValue = readValue();
            assertCellEnd();
            CellValue value = new CellValue(editorValue);
            model.setValueAt(value, pointer);
        }
        Map<CellRange, Color> rightColorsMap = readColorMap();
        Map<CellRange, Color> bottomColorsMap = readColorMap();
        Map<CellRange, Color> backgroundColorsMap = readColorMap();

        Map<CellRange, Boolean> rightLineNeedMap = readBoolMap();
        Map<CellRange, Boolean> bottomLineNeedMap = readBoolMap();
        colorModel.setMaps(rightColorsMap, bottomColorsMap, backgroundColorsMap, rightLineNeedMap, bottomLineNeedMap);
        return new SpreadSheetTable(model, colorModel);
    }

    private Map<CellRange, Color> readColorMap() throws IOException, ImportFormatException {
        Map<CellRange, Color> colorMap = new HashMap<>();
        line = reader.readLine();
        current = 0;
        int size = readInt();
        line = reader.readLine();
        current = 0;
        for (int i = 0; i < size; i++) {
            int startRow = readInt();
            int startColumn = readInt();
            int endRow = readInt();
            int endColumn = readInt();
            int rgb = readInt();
            CellRange range = new CellRange(startRow, startColumn, endRow, endColumn);
            Color color = new Color(rgb);
            colorMap.put(range, color);
        }
        return colorMap;
    }

    private Map<CellRange, Boolean> readBoolMap() throws IOException, ImportFormatException {
        Map<CellRange, Boolean> boolMap = new HashMap<>();
        line = reader.readLine();
        current = 0;
        int size = readInt();
        line = reader.readLine();
        current = 0;
        for (int i = 0; i < size; i++) {
            int startRow = readInt();
            int startColumn = readInt();
            int endRow = readInt();
            int endColumn = readInt();
            boolean value = Boolean.valueOf(readValue());
            CellRange range = new CellRange(startRow, startColumn, endRow, endColumn);
            boolMap.put(range, value);
        }
        return boolMap;
    }

    private int readInt() throws ImportFormatException {
        return Integer.parseInt(readValue());
    }

    private String readValue() throws ImportFormatException {
        int len = readLength();
        assertValueBegin();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(currentChar());
            current++;
        }
        assertValueEnd();
        return builder.toString();
    }

    private int readLength() {
        StringBuilder builder = new StringBuilder();
        while (Character.isDigit(currentChar())) {
            builder.append(currentChar());
            current++;
        }
        return Integer.parseInt(builder.toString());
    }

    private char currentChar() {
        return line.charAt(current);
    }

    private void assertValueBegin() throws ImportFormatException {
        check(TableExporter.VALUE_BEGIN_TAG);
    }

    private void assertValueEnd() throws ImportFormatException {
        check(TableExporter.VALUE_END_TAG);
    }

    private void assertCellBegin() throws ImportFormatException {
        check(TableExporter.CELL_VALUE_BEGIN_TAG);
    }

    private void assertCellEnd() throws ImportFormatException {
        check(TableExporter.CELL_VALUE_END_TAG);
    }

    private void check(char c) throws ImportFormatException {
        if (currentChar() != c) {
            throw new ImportFormatException();
        }
        current++;
    }
}
