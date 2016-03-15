package cells.iterator.cell;

import cells.pointer.CellPointer;
import cells.iterator.IterationStrategy;

import java.util.function.BiFunction;

/**
 * @author Dmitriy Tseyler
 */
public enum CellIterationStrategy implements IterationStrategy<CellPointer, AbstractCellIterator> {
    COLUMN_ROW() {
        @Override
        boolean firstCheck(CellPointer previous, AbstractCellIterator iterator) {
            return iterator.needChangeColumn(previous.getColumn());
        }

        @Override
        boolean secondCheck(CellPointer previous, AbstractCellIterator iterator) {
            return iterator.needChangeRow(previous.getRow());
        }

        @Override
        CellPointer nextElementFirst(CellPointer previous, AbstractCellIterator iterator) {
            return CellPointer.getPointer(previous, 0, iterator.getOffset());
        }

        @Override
        CellPointer nextElementSecond(CellPointer previous, AbstractCellIterator iterator) {
            return CellPointer.getPointer(previous.getRow() + iterator.getOffset(), iterator.getBegin().getColumn());
        }
    },
    ROW_COLUMN {
        @Override
        boolean firstCheck(CellPointer previous, AbstractCellIterator iterator) {
            return iterator.needChangeRow(previous.getRow());
        }

        @Override
        boolean secondCheck(CellPointer previous, AbstractCellIterator iterator) {
            return iterator.needChangeColumn(previous.getColumn());
        }

        @Override
        CellPointer nextElementFirst(CellPointer previous, AbstractCellIterator iterator) {
            return CellPointer.getPointer(previous, iterator.getOffset(), 0);
        }

        @Override
        CellPointer nextElementSecond(CellPointer previous, AbstractCellIterator iterator) {
            return CellPointer.getPointer(iterator.getBegin().getRow(), previous.getColumn() + iterator.getOffset());
        }
    };

    public CellPointer nextOf(CellPointer previous, AbstractCellIterator iterator) {
        CellPointer next = null;
        if (firstCheck(previous, iterator)) {
            next = nextElementFirst(previous, iterator);
        } else if (secondCheck(previous, iterator)) {
            next = nextElementSecond(previous, iterator);
        }
        return next;
    }

    abstract boolean firstCheck(CellPointer previous, AbstractCellIterator iterator);
    abstract boolean secondCheck(CellPointer previous, AbstractCellIterator iterator);
    abstract CellPointer nextElementFirst(CellPointer previous, AbstractCellIterator iterator);
    abstract CellPointer nextElementSecond(CellPointer previous, AbstractCellIterator iterator);
}
