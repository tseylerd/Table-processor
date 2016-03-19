package ui.laf.span;

import cells.CellRange;
import cells.pointer.CellPointer;
import ui.cursor.CursorConfiguration;
import ui.cursor.CursorManager;
import ui.laf.span.managers.*;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

/**
 * Mode, which holds all parameters for spanning
 * @see SpanManager
 * @author Dmitriy Tseyler
 */
public enum SpanMode {
    DOWN(DownSpanManager::new) {
        @Override
        int getMouseCoordinate(Point point) {
            return point.y;
        }

        @Override
        int getRectangleCoordinate(Rectangle rectangle) {
            return rectangle.y;
        }

        @Override
        boolean isItNotLast(CellPointer pointer, JTable table) {
            return pointer.getRow() > 0 && pointer.getRow() < table.getRowCount();
        }

        @Override
        public Cursor getCursor() {
            return CursorManager.getInstance().getCursor(CursorConfiguration.SPAN_DOWN);
        }

        @Override
        public CellPointer getStartCell(CellPointer pointer) {
            return CellPointer.getPointer(pointer, -1, 0);
        }

        @Override
        public CellRange createCellRange(CellPointer start, CellRange range) {
            return new CellRange(range.getFirstRow(), range.getFirstColumn(), start.getRow(), range.getLastColumn());
        }
    },
    UP(UpSpanManager::new) {
        @Override
        int getMouseCoordinate(Point point) {
            return point.y;
        }

        @Override
        int getRectangleCoordinate(Rectangle rectangle) {
            return rectangle.y + rectangle.height;
        }

        @Override
        boolean isItNotLast(CellPointer pointer, JTable table) {
            return pointer.getRow() > -1 && pointer.getRow() < table.getRowCount() - 1;
        }
        @Override
        public Cursor getCursor() {
            return CursorManager.getInstance().getCursor(CursorConfiguration.SPAN_UP);
        }

        @Override
        public CellPointer getStartCell(CellPointer pointer) {
            return CellPointer.getPointer(pointer, 1, 0);
        }

        @Override
        public CellRange createCellRange(CellPointer start, CellRange selectedRange) {
            return new CellRange(start.getRow(), selectedRange.getFirstColumn(), selectedRange.getLastRow(), selectedRange.getLastColumn());
        }
    },
    LEFT(LeftSpanManager::new) {
        @Override
        int getMouseCoordinate(Point point) {
            return point.x;
        }

        @Override
        int getRectangleCoordinate(Rectangle rectangle) {
            return rectangle.x + rectangle.width;
        }

        @Override
        boolean isItNotLast(CellPointer pointer, JTable table) {
            return pointer.getColumn() > 0 && pointer.getColumn() < table.getColumnCount() - 1;
        }
        @Override
        public Cursor getCursor() {
            return CursorManager.getInstance().getCursor(CursorConfiguration.SPAN_LEFT);
        }

        @Override
        public CellPointer getStartCell(CellPointer pointer) {
            return CellPointer.getPointer(pointer, 0, 1);
        }

        @Override
        public CellRange createCellRange(CellPointer start, CellRange selectedRange) {
            return new CellRange(selectedRange.getFirstRow(), start.getColumn(), selectedRange.getLastRow(), selectedRange.getLastColumn());
        }
    },
    RIGHT(RightSpanManager::new) {
        @Override
        int getMouseCoordinate(Point point) {
            return point.x;
        }

        @Override
        int getRectangleCoordinate(Rectangle rectangle) {
            return rectangle.x;
        }

        @Override
        boolean isItNotLast(CellPointer pointer, JTable table) {
            return pointer.getColumn() < table.getColumnCount() && pointer.getColumn() > 0;
        }
        @Override
        public Cursor getCursor() {
            return CursorManager.getInstance().getCursor(CursorConfiguration.SPAN_RIGHT);
        }

        @Override
        public CellPointer getStartCell(CellPointer pointer) {
            return CellPointer.getPointer(pointer, 0, -1);
        }

        @Override
        public CellRange createCellRange(CellPointer start, CellRange selectedRange) {
            return new CellRange(selectedRange.getFirstRow(), selectedRange.getFirstColumn(), selectedRange.getLastRow(), start.getColumn());
        }
    };

    private final Supplier<SpanManager> supplier;

    SpanMode(Supplier<SpanManager> supplier) {
        this.supplier = supplier;
    }

    private boolean compare(Point point, Rectangle rectangle, CellPointer pointer, JTable table) {
        return isMouseCoordinateOnBorder(getRectangleCoordinate(rectangle), getMouseCoordinate(point)) && isItNotLast(pointer, table);
    }

    public SpanManager createManager() {
        return supplier.get();
    }

    abstract int getRectangleCoordinate(Rectangle rectangle);
    abstract int getMouseCoordinate(Point point);
    abstract boolean isItNotLast(CellPointer pointer, JTable table);
    public abstract CellPointer getStartCell(CellPointer pointer);
    public abstract Cursor getCursor();
    public abstract CellRange createCellRange(CellPointer start, CellRange selectedRange);

    public static SpanMode getSpanMode(Point point, Rectangle cellRect, CellPointer pointer, JTable table) {
        for (SpanMode spanMode : SpanMode.values()) {
            if (spanMode.compare(point, cellRect, pointer, table)) {
                return spanMode;
            }
        }
        return null;
    }

    private static boolean isMouseCoordinateOnBorder(int cellRectCoordinate, int mouseCoordinate) {
        return Math.abs(cellRectCoordinate - mouseCoordinate) < 4;
    }
}
