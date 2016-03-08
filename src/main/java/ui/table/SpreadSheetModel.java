package ui.table;

import cells.CellValue;
import cells.CellsConnectionModel;
import math.calculator.ExpressionCalculator;
import cells.CellPointer;
import cells.CellRange;
import util.Util;

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
    private final List<TableModelListener> tableModelListeners;
    private final CellValue[][] values; //// TODO: 06.03.16 Variable length of rows and columns
    private final CellsConnectionModel cellsConnectionModel; // TODO: 06.03.16 Should be one model for cells
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
            values[i][0] = new CellValue(String.valueOf(i)); // TODO: 07.03.16 Move row number to ui
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
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) { // TODO: 07.03.16 Cyclic references
        CellValue cellValue = (CellValue)aValue;
        try {
            String eval = evaluate(cellValue.getEditorValue());
            cellValue.setValue(eval);
            cellValue.setExpression(cellValue.getEditorValue());
            cellValue.setErrorState(false);
        } catch (ParseException e) {
            cellValue.setErrorState(true);
        }
        values[rowIndex][columnIndex] = cellValue;
        Set<CellPointer> pointers = calculator.getPointers();
        Set<CellRange> ranges = calculator.getRanges();
        CellPointer pointer = new CellPointer(rowIndex, columnIndex);
        cellsConnectionModel.subscribe(pointer, pointers, ranges);
        cellsConnectionModel.cellChanged(pointer);
    }

    private String evaluate(String s) throws ParseException {
        calculator.reset();
        if (s != null && !s.isEmpty() && s.charAt(0) == '=') {
            return calculator.calculate(s.substring(1));
        }
        return s;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return Util.columnNameByIndex(columnIndex); //// TODO: 07.03.16 cache
    }

    private void fireTableModelListeners() {
        tableModelListeners.forEach(tableModelListener -> tableModelListener.tableChanged(null));
    }

    public double getNumber(int row, int column) {
        CellValue value = (CellValue) getValueAt(row, column);
        return Double.parseDouble(value.getRendererValue());
    }

    public double getNumber(CellPointer pointer) {
        return getNumber(pointer.getRow(), pointer.getColumn());
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

