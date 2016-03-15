package storage;

import cells.CellValue;
import cells.pointer.CellPointer;
import ui.laf.grid.TableColorModel;
import ui.table.SpreadSheetModel;
import ui.table.SpreadSheetTable;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;

/**
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
        TableColorModel colorModel = new TableColorModel(model);
        while ((line = reader.readLine()) != null) {
            current = 0;
            assertCellBegin();
            current++;
            int row = readInt();
            int column = readInt();
            CellPointer pointer = CellPointer.getPointer(row, column);
            String editorValue = readValue();
            Color lowerLine = new Color(readInt());
            Color rightColor = new Color(readInt());
            Color background = new Color(readInt());
            boolean needLower = Boolean.valueOf(readValue());
            boolean needRight = Boolean.valueOf(readValue());
            assertCellEnd();
            current++;
            CellValue value = new CellValue(editorValue);
            colorModel.setBackgroundColor(pointer, background);
            colorModel.setNeedLowerLine(pointer, needLower);
            colorModel.setNeedRightLine(pointer, needRight);
            colorModel.setRightLineColor(pointer, rightColor);
            colorModel.setLowerLineColor(pointer, lowerLine);
            model.setValueAt(value, pointer);
        }
        return new SpreadSheetTable(model, colorModel);
    }

    private int readInt() throws ImportFormatException {
        return Integer.parseInt(readValue());
    }

    private String readValue() throws ImportFormatException {
        int len = readLength();
        assertValueBegin();
        current++;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(currentChar());
            current++;
        }
        assertValueEnd();
        current++;
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
    }
}
