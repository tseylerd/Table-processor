package storage;

import ui.laf.ProcessorUIDefaults;

import java.lang.reflect.Array;

/**
 * @author Dmitriy Tseyler
 */
public class LazyDynamicArray<T> {
    private static final double MULTIPLIER = 1;

    private Object[][] values;
    private int rows;
    private int columns;
    private final Class<T> tClass;

    public LazyDynamicArray(int rows, int columns, Class<T> tClass) {
        //noinspection unchecked
        Class<T[]> arrayClass = (Class<T[]>)Array.newInstance(tClass, 0).getClass();
        this.values = (Object[][])Array.newInstance(arrayClass, getIncreasedValue(rows));
        this.rows = rows;
        this.columns = columns;
        this.tClass = tClass;
    }

    public void set(int row, int column, T value) {
        if (values[row] == null) {
            values[row] = (Object[])Array.newInstance(tClass, getIncreasedValue(columns));
        }
        values[row][column] = value;
    }

    public T get(int row, int column) {
        if (values[row] == null) {
            return null;
        }
        //noinspection unchecked
        return (T)values[row][column];
    }

    public void setIfExists(int row, int column, T value) {
        if (values[row] != null && values[row][column] != null) {
            set(row, column, value);
        }
    }

    public boolean rowExists(int row) {
        return values[row] != null;
    }

    public void addRow() {
        rows++;
        if (rows >= getIncreasedValue(rows)) {
            values = copyRows(values, rows, columns);
        }
        int lastRow = rows - 1;
        for (int i = 0; i < columns; i++) {
            if (values[lastRow] != null) {
                values[rows - 1][i] = null;
            }
        }
    }

    public void addColumn() {
        columns++;
        if (columns >= getIncreasedValue(columns)) {
            values = copyColumns(values, rows, columns);
        }
        for (int i = 0; i < rows; i++) {
            if (values[i] != null) {
                values[i][columns - 1] = null;
            }
        }
    }

    public void removeRow() {
        rows--;
    }

    public void removeColumn() {
        columns--;
    }

    public int rowCount() {
        return rows;
    }

    public int columnCount() {
        return columns;
    }

    private static<T> T[][] copyRows(T[][] from, int rows, int columns) {
        return copy(from, getIncreasedValue(rows), columns);
    }

    private static<T> T[][] copyColumns(T[][] from, int rows, int columns) {
        return copy(from, rows, getIncreasedValue(columns));
    }

    private static<T> T[][] copy(T[][] from, int rows, int columns) {
        Class<?> arrayClass = from.getClass();
        //noinspection unchecked
        T[][] newArray = (T[][])Array.newInstance(arrayClass.getComponentType(), rows);
        for (int i = 0; i < from.length; i++) {
            if (from[i] != null) {
                //noinspection unchecked
                newArray[i] = (T[])Array.newInstance(from[i].getClass().getComponentType(), columns);
                System.arraycopy(from[i], 0, newArray[i], 0, from[i].length);
            }
        }
        return newArray;
    }

    public static int getIncreasedValue(int value) {
        return (int) Math.min(Math.round(value * MULTIPLIER), ProcessorUIDefaults.MAX_ROWS_COLUMNS);
    }
}
