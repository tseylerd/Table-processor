package ui.laf.span;

import cells.pointer.CellPointer;
import ui.laf.span.managers.*;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

/**
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
            return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        }

        @Override
        public CellPointer getStartCell(CellPointer pointer) {
            return CellPointer.getPointerWithOffset(pointer, -1, 0);
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
            return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        }

        @Override
        public CellPointer getStartCell(CellPointer pointer) {
            return CellPointer.getPointerWithOffset(pointer, 1, 0);
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
            return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        }

        @Override
        public CellPointer getStartCell(CellPointer pointer) {
            return CellPointer.getPointerWithOffset(pointer, 0, 1);
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
            return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        }

        @Override
        public CellPointer getStartCell(CellPointer pointer) {
            return CellPointer.getPointerWithOffset(pointer, 0, -1);
        }
    };

    private final Supplier<SpanManager> supplier;

    SpanMode(Supplier<SpanManager> supplier) {
        this.supplier = supplier;
    }

    public boolean compare(Point point, Rectangle rectangle, CellPointer pointer, JTable table) {
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

    public static SpanMode getSpanMode(Point point, Rectangle cellRect, CellPointer pointer, JTable table) {
        for (SpanMode spanMode : SpanMode.values()) {
            if (spanMode.compare(point, cellRect, pointer, table)) {
                return spanMode;
            }
        }
        return null;
    }

    private static boolean isMouseCoordinateOnBorder(int cellRectCoordinate, int mouseCoordinate) {
        return Math.abs(cellRectCoordinate - mouseCoordinate) < 3;
    }
}
