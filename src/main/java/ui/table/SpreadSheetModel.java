package ui.table;

import cells.CellValue;
import cells.connection.CellsConnectionModel;
import cells.connection.PointerNode;
import math.calculator.ExpressionParser;
import cells.pointer.CellPointer;
import cells.CellRange;
import math.calculator.Lexer.LexerValue;
import math.calculator.expression.Expression;
import ui.table.exceptions.CyclicReferenceException;
import ui.table.exceptions.EmptyValueException;
import ui.table.exceptions.InvalidCellPointerException;
import util.Util;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetModel implements TableModel {
    public static final CellValue EMPTY = new CellValue();

    private final List<TableModelListener> tableModelListeners;
    private final HashMap<CellPointer, CellValue> values; //// TODO: 06.03.16 Variable length of rows and columns
    private final CellsConnectionModel cellsConnectionModel; // TODO: 06.03.16 Should be one model for cells
    private final ExpressionParser parser;

    private int rowCount;
    private int columnCount;

    public SpreadSheetModel(int rowCount, int columnCount) {
        cellsConnectionModel = new CellsConnectionModel(this);
        parser = new ExpressionParser(this);
        values = new HashMap<>();
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

    public void recalculate(PointerNode pointerNode) {
        CellValue value = values.get(pointerNode.getPointer());
        recalculateValue(value, pointerNode);
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CellValue value = values.get(CellPointer.getPointer(rowIndex, columnIndex));
        if (value == null) {
            value = EMPTY;
        }
        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        CellValue cellValue = (CellValue)aValue;
        CellPointer pointer = CellPointer.getPointer(rowIndex, columnIndex);
        cellValue = getTrueValue(cellValue);
        values.put(pointer, cellValue);
        try {
            Set<CellPointer> pointers = parser.getPointers();
            Set<CellRange> ranges = parser.getRanges();
            cellsConnectionModel.subscribe(pointer, pointers, ranges);
            cellsConnectionModel.cellChanged(new PointerNode(pointer));
        } catch (CyclicReferenceException e) {
            cellValue.setErrorState(true);
        }
        cellsConnectionModel.resetErrors();
        fireTableModelListeners(rowIndex);
    }

    private void recalculateValue(CellValue cellValue, PointerNode pointer) {
        cellValue = getTrueValue(cellValue);
        values.put(pointer.getPointer(), cellValue);
        try {
            cellsConnectionModel.cellChanged(pointer);
        } catch (CyclicReferenceException e) {
            cellValue.setErrorState(true);
        }
        fireTableModelListeners(pointer.getPointer().getRow());
    }

    private CellValue getTrueValue(CellValue cellValue) {
        try {
            Expression expression = evaluate(cellValue);
            LexerValue value = expression.calculate();
            cellValue.setRendererValue(value.getStringValue());
            cellValue.setExpression(expression);
            cellValue.setErrorState(false);
        } catch (NumberFormatException | InvalidCellPointerException | EmptyValueException e) { // todo reduce exceptions count
            cellValue.setErrorState(true);
        }
        return cellValue;
    }

    private Expression evaluate(CellValue value) {
        if (value.getExpression() != null) {
            return value.getExpression();
        }
        return parser.parse(value.getEditorValue());
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

    public CellValue getValueAt(CellPointer pointer) {
        return (CellValue)getValueAt(pointer.getRow(), pointer.getColumn());
    }

    public void setValueAt(CellValue value, CellPointer pointer) {
        setValueAt(value, pointer.getRow(), pointer.getColumn());
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

