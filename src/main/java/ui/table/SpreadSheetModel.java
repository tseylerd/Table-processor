package ui.table;

import cells.CellValue;
import cells.connection.CellsConnectionModel;
import cells.connection.PointerNode;
import math.calculator.ExpressionParser;
import cells.pointer.CellPointer;
import cells.CellRange;
import math.calculator.ParserException;
import math.calculator.lexer.LexerValue;
import math.calculator.expression.Expression;
import ui.table.error.Error;
import ui.table.exceptions.*;
import util.Util;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.*;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetModel implements TableModel {
    public static final CellValue EMPTY = new CellValue();

    private final List<TableModelListener> tableModelListeners;
    private final Map<CellPointer, CellValue> values;
    private final CellsConnectionModel cellsConnectionModel;
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
        if (rowIndex >= rowCount || columnIndex >= columnCount) {
            throw new IndexOutOfBoundsException();
        }

        CellValue value = values.get(CellPointer.getPointer(rowIndex, columnIndex));
        if (value == null) {
            value = EMPTY;
        }
        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        setValueAt((CellValue)aValue, CellPointer.getPointer(rowIndex, columnIndex));
    }

    private void recalculateValue(CellValue cellValue, PointerNode pointer) {
        cellValue = getTrueValue(cellValue);
        values.put(pointer.getPointer(), cellValue);
        try {
            cellsConnectionModel.cellChanged(pointer);
        } catch (CyclicReferenceException e) {
            cellValue.setError(Error.CYCLIC_REFERENCE);
            cellValue.setRendererValue("");
        }
        fireTableModelListeners(pointer.getPointer().getRow());
    }

    private CellValue getTrueValue(CellValue cellValue) {
        try {
            Expression expression = evaluate(cellValue);
            LexerValue value = expression.calculate();
            cellValue.setRendererValue(value.getStringValue());
            cellValue.setExpression(expression);
            cellValue.setError(null);
        } catch (SpreadSheetException e) {
            cellValue.setRendererValue("");
            cellValue.setError(e.getError());
        }
        return cellValue;
    }

    private Expression evaluate(CellValue value) throws ParserException {
        if (value.getExpression() != null) {
            return value.getExpression();
        }
        return parser.parse(value.getEditorValue());
    }

    @Override
    public String getColumnName(int columnIndex) {
        return Util.columnNameByIndex(columnIndex);
    }

    private void fireTableModelListeners(int row) {
        TableModelEvent event = new TableModelEvent(this, row);
        tableModelListeners.forEach(tableModelListener -> tableModelListener.tableChanged(event));
    }

    private void fireTableModelListeners(TableModelEvent event) {
        tableModelListeners.forEach(tableModelListener -> tableModelListener.tableChanged(event));
    }

    public double getNumber(int row, int column) {
        CellValue value = (CellValue) getValueAt(row, column);
        try {
            return Double.parseDouble(value.getRendererValue());
        } catch (NumberFormatException e) {
            throw new NumberParseException(value.getRendererValue());
        }
    }

    public double getNumber(CellPointer pointer) {
        return getNumber(pointer.getRow(), pointer.getColumn());
    }

    public CellValue getValueAt(CellPointer pointer) {
        return (CellValue)getValueAt(pointer.getRow(), pointer.getColumn());
    }

    public void setValueAt(CellValue cellValue, CellPointer pointer) {
        cellValue = getTrueValue(cellValue);
        values.put(pointer, cellValue);
        try {
            Set<CellPointer> pointers = parser.getPointers();
            Set<CellRange> ranges = parser.getRanges();
            cellsConnectionModel.subscribe(pointer, pointers, ranges);
            cellsConnectionModel.cellChanged(new PointerNode(pointer));
        } catch (CyclicReferenceException e) {
            cellValue.setError(Error.CYCLIC_REFERENCE);
        }
        cellsConnectionModel.resetErrors();
        fireTableModelListeners(pointer.getRow());
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

    public void addRow() {
        rowCount++;
        fireTableRowsInserted(rowCount, rowCount);
    }

    public void addColumn(TableColumnModel columnModel) {
        columnModel.addColumn(new TableColumn(columnCount++));
        fireTableStructureChanged();
    }

    public void removeRow() {
        rowCount--;
        CellRange range = new CellRange(rowCount, 0, rowCount, columnCount);
        for (CellPointer pointer : range) {
            values.remove(pointer);
            setValueAt(EMPTY, pointer);
        }
        fireTableRowsDeleted(rowCount, rowCount);
    }

    public void removeColumn(TableColumnModel model) {
        columnCount--;
        CellRange range = new CellRange(0, columnCount, rowCount, columnCount);
        for (CellPointer pointer : range) {
            values.remove(pointer);
            setValueAt(EMPTY, pointer);
        }
        model.removeColumn(model.getColumn(columnCount));
        fireTableStructureChanged();
    }

    private void fireTableRowsDeleted(int firstRow, int lastRow) {
        fireTableModelListeners(new TableModelEvent(this, firstRow, lastRow,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
    }

    public void fireTableRowsInserted(int firstRow, int lastRow) {
        fireTableModelListeners(new TableModelEvent(this, firstRow, lastRow,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    public void fireTableStructureChanged() {
        fireTableModelListeners(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }
}

