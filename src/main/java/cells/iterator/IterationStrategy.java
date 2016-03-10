package cells.iterator;

import cells.CellPointer;

import java.util.function.BiFunction;

/**
 * @author Dmitriy Tseyler
 */
enum  IterationStrategy {
    COLUMN_ROW(((pointer, iterator) -> iterator.needChangeColumn(pointer.getColumn())),
            (pointer, iterator) -> iterator.needChangeRow(pointer.getRow()),
            (cellPointer, iterator) -> new CellPointer(cellPointer, 0, iterator.getOffset()),
            (cellPointer, iterator) -> new CellPointer(cellPointer, iterator.getOffset(), 0)),
    ROW_COLUMN((pointer, iterator) -> iterator.needChangeRow(pointer.getRow()),
            (pointer, iterator) -> iterator.needChangeColumn(pointer.getColumn()),
            (cellPointer, iterator) -> new CellPointer(cellPointer, iterator.getOffset(), 0),
            (cellPointer, iterator) -> new CellPointer(cellPointer, 0, iterator.getOffset()));

    private final BiFunction<CellPointer, CellIterator, Boolean> firstCheck;
    private final BiFunction<CellPointer, CellIterator, Boolean> secondCheck;
    private final BiFunction<CellPointer, CellIterator, CellPointer> firstFunction;
    private final BiFunction<CellPointer, CellIterator, CellPointer> secondFunction;

    IterationStrategy(BiFunction<CellPointer, CellIterator, Boolean> firstCheck,
                      BiFunction<CellPointer, CellIterator, Boolean> secondCheck,
                      BiFunction<CellPointer, CellIterator, CellPointer> firstFunction,
                      BiFunction<CellPointer, CellIterator, CellPointer> secondFunction)
    {
        this.firstCheck = firstCheck;
        this.secondCheck = secondCheck;
        this.firstFunction = firstFunction;
        this.secondFunction = secondFunction;
    }

    CellPointer nextOf(CellPointer previous, CellIterator iterator) {
        CellPointer next = null;
        if (firstCheck.apply(previous, iterator)) {
            next = firstFunction.apply(previous, iterator);
        } else if (secondCheck.apply(previous, iterator)) {
            next = secondFunction.apply(previous, iterator);
        }
        return next;
    }
}
