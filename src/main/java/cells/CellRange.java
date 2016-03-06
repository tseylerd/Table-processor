package main.java.cells;

import java.util.Iterator;

/**
 * @author Dmitriy Tseyler
 */
public class CellRange implements Iterable<CellPointer> {
    private final CellPointer begin;
    private final CellPointer end;

    public CellRange(CellPointer begin, CellPointer end) {
        this.begin = begin;
        this.end = end;
    }

    public CellPointer getBegin() {
        return begin;
    }

    public CellPointer getEnd() {
        return end;
    }

    @Override
    public Iterator<CellPointer> iterator() {
        return new CellIterator(this);
    }
}
