package ui.table;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetModel implements TableModel {
    private static final int ENGLISH_CHARACTERS_COUNT = 26;

    private final List<TableModelListener> tableModelListeners;
    private final CellValue[][] values;
    private int rowCount;
    private int columnCount;

    public SpreadSheetModel(int rowCount, int columnCount) {
        rowCount++;
        columnCount++;
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
        values[rowIndex][columnIndex] = (cellValue);
        fireTableModelListeners();
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0)
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

