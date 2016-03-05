package ui.table;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetModel implements TableModel {
    private final List<TableModelListener> tableModelListeners;
    private final List<List<Entity>> values;
    private int rowCount;
    private int columnCount;

    public SpreadSheetModel(int rowCount, int columnCount) {
        values = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            values.add(new ArrayList<>());
            for (int j = 0; j < columnCount; j++) {
                values.get(i).add(new Entity());
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
        return Entity.class;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return values.get(rowIndex).get(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        values.get(rowIndex).remove(columnIndex);
        values.get(rowIndex).add(columnIndex, (Entity)aValue);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return null;
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

