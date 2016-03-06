package main.java.ui.table;

import main.java.cells.CellValue;
import main.java.cells.CellsConnectionModel;
import main.java.math.calculator.ExpressionCalculator;
import main.java.cells.CellPointer;
import main.java.cells.CellRange;
import main.java.util.Util;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetModel implements TableModel {
    private static final int ENGLISH_CHARACTERS_COUNT = 26;

    private final List<TableModelListener> tableModelListeners;
    private final CellValue[][] values; //// TODO: 06.03.16 Variable length of rows and columns
    private final CellsConnectionModel cellsConnectionModel; // TODO: 06.03.16 Should be one model for main.java.cells
    private final ExpressionCalculator calculator;

    private int rowCount;
    private int columnCount;

    public SpreadSheetModel(int rowCount, int columnCount) {
        rowCount++;
        columnCount++;
        cellsConnectionModel = new CellsConnectionModel(this);
        calculator = new ExpressionCalculator(this);
        values = new CellValue[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            values[i][0] = new CellValue(String.valueOf(i));
        }
        for (int i = 0; i < rowCount; i++) {
            for (int j = 1; j < columnCount; j++) {
                values[i][j] = new CellValue();
            }
        }
        tableModelListeners = new ArrayList<>();
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return CellValue.class;
    }

    public void recalculate(CellPointer pointer) {
        int row = pointer.getRow();
        int column = pointer.getColumn();
        CellValue value = values[row][column];
        setValueAt(value, row, column);
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return values[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        CellValue cellValue = (CellValue)aValue;
        try {
            String eval = evaluate(cellValue.getEditorValue());
            cellValue.setValue(eval);
            cellValue.setExpression(cellValue.getEditorValue());
            cellValue.setErrorState(false);
            values[rowIndex][columnIndex] = cellValue;
        } catch (ParseException e) {
            cellValue.setErrorState(true);
        }
        Set<CellPointer> pointers = calculator.getPointers();
        Set<CellRange> ranges = calculator.getRanges();
        CellPointer pointer = new CellPointer(rowIndex, columnIndex);
        cellsConnectionModel.subscribe(pointer, pointers, ranges);
        cellsConnectionModel.cellChanged(new CellPointer(rowIndex, columnIndex));
    }

    private String evaluate(String s) throws ParseException {
        calculator.reset();
        if (!s.isEmpty() && s.charAt(0) == '=') {
            return calculator.calculate(Util.addSpecialCharacters(s.substring(1)));
        }
        return s;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) //// TODO: 06.03.16 Cache
            return "";

        columnIndex--;
        int count = columnIndex / ENGLISH_CHARACTERS_COUNT;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append('A');
        }

        builder.append((char)((columnIndex % ENGLISH_CHARACTERS_COUNT) + 65));
        return builder.toString();
    }

    private void fireTableModelListeners() {
        tableModelListeners.forEach(tableModelListener -> tableModelListener.tableChanged(null));
    }

    public double getNumber(int row, int column) {
        CellValue value = (CellValue) getValueAt(row, column);
        return Double.parseDouble(value.getRendererValue());
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        tableModelListeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        tableModelListeners.remove(l);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }
}

