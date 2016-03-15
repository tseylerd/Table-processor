package ui.laf.span.spanner;

import cells.pointer.CellPointer;
import cells.CellRange;
import cells.CellValue;
import math.calculator.lexer.LexerValue;
import ui.table.SpreadSheetModel;
import ui.table.exceptions.NumberParseException;
import util.Util;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author Dmitriy Tseyler
 */
public enum Spanner {
    DOWN(CellRange::columnRangeIterator) {
        @Override
        int getOffset(CellPointer from, CellPointer to) {
            return to.getRow() - from.getRow();
        }

        @Override
        CellValue moveWithOffset(CellValue toMove, int offset) {
            return Util.moveImmutably(toMove, offset, 0);
        }
    },
    UP(CellRange::inverseColumnRangeIterator) {
        @Override
        int getOffset(CellPointer from, CellPointer to) {
            return to.getRow() - from.getRow();
        }

        @Override
        CellValue moveWithOffset(CellValue toMove, int offset) {
            return Util.moveImmutably(toMove, offset, 0);
        }
    },
    LEFT(CellRange::inverseRangeIterator) {
        @Override
        int getOffset(CellPointer from, CellPointer to) {
            return to.getColumn() - from.getColumn();
        }

        @Override
        CellValue moveWithOffset(CellValue toMove, int offset) {
            return Util.moveImmutably(toMove, 0, offset);
        }
    },
    RIGHT(CellRange::rangeIterator) {
        @Override
        int getOffset(CellPointer from, CellPointer to) {
            return to.getColumn() - from.getColumn();
        }

        @Override
        CellValue moveWithOffset(CellValue toMove, int offset) {
            return Util.moveImmutably(toMove, 0, offset);
        }
    };

    private final Function<CellRange, Iterator<CellRange>> rangeIteratorGetter;

    Spanner(Function<CellRange, Iterator<CellRange>> rangeIteratorGetter) {
        this.rangeIteratorGetter = rangeIteratorGetter;
    }

    abstract int getOffset(CellPointer from, CellPointer to);
    abstract CellValue moveWithOffset(CellValue toMove, int offset);

    public void span(SpreadSheetModel model, CellRange from, CellRange to) {
        Iterator<CellRange> fromIterator = rangeIteratorGetter.apply(from);
        Iterator<CellRange> toIterator = rangeIteratorGetter.apply(to);
        while (fromIterator.hasNext()) {
            CellRange fromRange = fromIterator.next();
            CellRange toRange = toIterator.next();
            double nextOffset = findLinearDependency(fromRange, model);
            if (nextOffset != 0) {
                spanLinearly(fromRange, toRange, nextOffset, model);
            } else {
                span(fromRange, toRange, model);
            }
        }
    }

    private double findLinearDependency(CellRange range, SpreadSheetModel model) {
        if (range.size() <= 1) {
            return 0;
        }
        Iterator<CellPointer> iterator = range.iterator();
        CellPointer first = iterator.next();
        CellValue firstValue = model.getValueAt(first);
        if (firstValue.containsExpression()) {
            return 0;
        }
        CellPointer second = iterator.next();
        CellValue secondValue = model.getValueAt(second);
        if (secondValue.containsExpression()) {
            return 0;
        }
        double offset;
        try {
            offset = model.getNumber(second) - model.getNumber(first);
        } catch (NumberParseException e) {
            return 0;
        }
        while (iterator.hasNext()) {
            CellPointer pointer = iterator.next();
            CellValue value = model.getValueAt(pointer);
            if (value.containsExpression()) {
                return 0;
            }
            double newOffset;
            try {
                newOffset = model.getNumber(pointer) - model.getNumber(second);
            } catch (NumberParseException e) {
                return 0;
            }
            if (Double.compare(offset, newOffset) != 0) {
                return 0;
            }
            second = pointer;
        }
        return offset;
    }

    private void spanLinearly(CellRange from, CellRange to, double toAdd, SpreadSheetModel model) {
        Iterator<CellPointer> fromIterator = from.iterator();
        Iterator<CellPointer> toIterator = to.iterator();
        while (toIterator.hasNext()) {
            if (!fromIterator.hasNext()) {
                fromIterator = from.iterator();
            }
            CellPointer fromPointer = fromIterator.next();
            CellPointer toPointer = toIterator.next();
            int offset = getOffset(fromPointer, toPointer);
            double number = model.getNumber(fromPointer);
            double spanned = number + offset * toAdd;
            model.setValueAt(new CellValue(LexerValue.FORMAT.format(spanned)), toPointer.getRow(), toPointer.getColumn());
        }
    }

    private void span(CellRange from, CellRange to, SpreadSheetModel model) {
        Iterator<CellPointer> fromIterator = from.iterator();
        Iterator<CellPointer> toIterator = to.iterator();
        while (toIterator.hasNext()) {
            if (!fromIterator.hasNext()) {
                fromIterator = from.iterator();
            }
            CellPointer fromPointer = fromIterator.next();
            CellPointer toPointer = toIterator.next();
            int offset = getOffset(fromPointer, toPointer);
            CellValue value = model.getValueAt(fromPointer);
            CellValue moved = moveWithOffset(value, offset);
            model.setValueAt(moved, toPointer);
        }
    }
}
