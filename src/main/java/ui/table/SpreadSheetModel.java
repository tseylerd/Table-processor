package ui.table;

import cells.CellValue;
import cells.CellsConnectionModel;
import math.calculator.ExpressionCalculator;
import cells.CellPointer;
import cells.CellRange;
import ui.table.exceptions.CyclicReferenceException;
import util.Util;

import javax.swing.event.TableModelEvent;
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
        cellsConnectionModel = new CellsConnectionModel(this);
        calculator = new ExpressionCalculator(this);
        values = new CellValue[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
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
        recalculateValue(value, row, column);
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
        CellPointer pointer = new CellPointer(rowIndex, columnIndex);
        cellValue = getTrueValue(cellValue);
        values[rowIndex][columnIndex] = cellValue;
        try {
            Set<CellPointer> pointers = calculator.getPointers();
            Set<CellRange> ranges = calculator.getRanges();
            cellsConnectionModel.subscribe(pointer, pointers, ranges);
            cellsConnectionModel.cellChanged(pointer);
        } catch (CyclicReferenceException e) {
            cellValue.setErrorState(true);
        }
        cellsConnectionModel.resetErrors();
        fireTableModelListeners(rowIndex);
    }

    private void recalculateValue(CellValue cellValue, int rowIndex, int columnIndex) {
        CellPointer pointer = new CellPointer(rowIndex, columnIndex);
        cellValue = getTrueValue(cellValue);
        values[rowIndex][columnIndex] = cellValue;
        try {
            cellsConnectionModel.cellChanged(pointer);
        } catch (CyclicReferenceException e) {
            cellValue.setErrorState(true);
        }
        fireTableModelListeners(rowIndex);
    }

    private CellValue getTrueValue(CellValue cellValue) {
        try {
            String eval = evaluate(cellValue.getEditorValue());
            cellValue.setValue(eval);
            cellValue.setErrorState(false);
        } catch (NumberFormatException e) {
            cellValue.setErrorState(true);
        }
        return cellValue;
    }
    private String evaluate(String s) {
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

    private void fireTableModelListeners(int row) {
        TableModelEvent event = new TableModelEvent(this, row);
        tableModelListeners.forEach(tableModelListener -> tableModelListener.tableChanged(event));
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
        return true;
    }
}

