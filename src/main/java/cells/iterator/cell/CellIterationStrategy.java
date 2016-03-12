package cells.iterator.cell;

import cells.pointer.CellPointer;
import cells.iterator.IterationStrategy;

import java.util.function.BiFunction;

/**
 * @author Dmitriy Tseyler
 */
public enum CellIterationStrategy implements IterationStrategy<CellPointer, AbstractCellIterator> {
    COLUMN_ROW(((pointer, iterator) -> iterator.needChangeColumn(pointer.getColumn())),
            (pointer, iterator) -> iterator.needChangeRow(pointer.getRow()),
            (cellPointer, iterator) -> CellPointer.getPointerWithOffset(cellPointer, 0, iterator.getOffset()),
            (cellPointer, iterator) -> CellPointer.getPointer(cellPointer.getRow() + iterator.getOffset(), iterator.getBegin().getColumn())),
    ROW_COLUMN((pointer, iterator) -> iterator.needChangeRow(pointer.getRow()),
            (pointer, iterator) -> iterator.needChangeColumn(pointer.getColumn()),
            (cellPointer, iterator) -> CellPointer.getPointerWithOffset(cellPointer, iterator.getOffset(), 0),
            (cellPointer, iterator) -> CellPointer.getPointer(iterator.getBegin().getRow(), cellPointer.getColumn() + iterator.getOffset()));

    private final BiFunction<CellPointer, AbstractCellIterator, Boolean> firstCheck;
    private final BiFunction<CellPointer, AbstractCellIterator, Boolean> secondCheck;
    private final BiFunction<CellPointer, AbstractCellIterator, CellPointer> firstFunction;
    private final BiFunction<CellPointer, AbstractCellIterator, CellPointer> secondFunction;

    CellIterationStrategy(BiFunction<CellPointer, AbstractCellIterator, Boolean> firstCheck,
                          BiFunction<CellPointer, AbstractCellIterator, Boolean> secondCheck,
                          BiFunction<CellPointer, AbstractCellIterator, CellPointer> firstFunction,
                          BiFunction<CellPointer, AbstractCellIterator, CellPointer> secondFunction)
    {
        this.firstCheck = firstCheck;
        this.secondCheck = secondCheck;
        this.firstFunction = firstFunction;
        this.secondFunction = secondFunction;
    }

    public CellPointer nextOf(CellPointer previous, AbstractCellIterator iterator) {
        CellPointer next = null;
        if (firstCheck.apply(previous, iterator)) {
            next = firstFunction.apply(previous, iterator);
        } else if (secondCheck.apply(previous, iterator)) {
            next = secondFunction.apply(previous, iterator);
        }
        return next;
    }
}
