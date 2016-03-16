package cells.connection;

import cells.CellRange;
import cells.pointer.CellPointer;
import storage.LazyDynamicArray;
import ui.table.SpreadSheetModel;
import ui.table.exceptions.CyclicReferenceException;

import javax.swing.event.TableModelEvent;
import java.util.*;

/**
 * @author Dmitriy Tseyler
 */
public class CellsConnectionModel {
    private final LazyDynamicArray<List> subscribed;
    private final LazyDynamicArray<List> references;

    private final SpreadSheetModel model;

    private boolean cycle;

    public CellsConnectionModel(SpreadSheetModel model) {
        subscribed = new LazyDynamicArray<>(model.getRowCount(), model.getColumnCount(), List.class);
        references = new LazyDynamicArray<>(model.getRowCount(), model.getColumnCount(), List.class);
        this.model = model;
        model.addTableModelListener(this::tableModelChanged);
    }

    public void cellChanged(PointerNode pointer) throws CyclicReferenceException {
        int row = pointer.getRow();
        int column = pointer.getColumn();
        if (subscribed.get(row, column) == null) {
            return;
        }

        if (pointer.isVisited()) {
            cycle = true;
            throw new CyclicReferenceException();
        }

        pointer.setVisited(true);
        //noinspection unchecked
        List<PointerNode> refs = subscribed.get(row, column);
        refs.forEach(model::recalculate);
        pointer.setVisited(false);

        if (cycle) {
            throw new CyclicReferenceException();
        }
    }


    public void resetErrors() {
        cycle = false;
    }

    private List<CellRange> getWhatINeedSubscribe(PointerNode pointer, List<CellRange> ranges) {
        int row = pointer.getRow();
        int column = pointer.getColumn();
        //noinspection unchecked
        List<CellRange> references = this.references.get(row, column);
        List<CellRange> toSubscribe = new ArrayList<>();
        if (references != null) {
            ranges.forEach(cellRange -> {
                if (!references.contains(cellRange)) {
                    toSubscribe.add(cellRange);
                }
            });
            int size = references.size();
            for (int i = size - 1; i >= 0; i--) {
                CellRange range = references.get(i);
                if (!ranges.contains(range)) {
                    clearSubscribtion(range, pointer);
                    references.remove(i);
                }
            }
            references.addAll(toSubscribe);
        } else {
            this.references.set(row, column, ranges);
            return ranges;
        }
        return toSubscribe;
    }

    private void clearSubscribtion(CellRange range, PointerNode node) {
        for (CellPointer pointer : range) {
            subscribed.get(pointer.getRow(), pointer.getColumn()).remove(node);
        }
    }

    public void subscribe(PointerNode pointer, List<CellRange> ranges) {
        List<CellRange> toSubscribe = getWhatINeedSubscribe(pointer, ranges);
        List<PointerNode> newRefs = new LinkedList<>();
        processRanges(pointer, toSubscribe, newRefs);
        references.set(pointer.getRow(), pointer.getColumn(), newRefs);
    }

    public void processRanges(PointerNode pointer, List<CellRange> ranges, List<PointerNode> newReferences) {
        for (CellRange range : ranges) {
            processRange(pointer, range, newReferences);
        }
    }

    private void processRange(PointerNode pointer, CellRange range, List<PointerNode> newRefs) {
        for (CellPointer cellPointer : range) {
            processReference(pointer, new PointerNode(cellPointer), newRefs);
        }
    }

    private void processReference(PointerNode mainPointer, PointerNode cellPointer, List<PointerNode> references) {
        int row = cellPointer.getRow();
        int column = cellPointer.getColumn();
        //noinspection unchecked
        List<PointerNode> recalculate = subscribed.get(row, column);
        if (recalculate == null) {
            recalculate = new LinkedList<>();
            subscribed.set(cellPointer.getPointer().getRow(), cellPointer.getPointer().getColumn(), recalculate);
        }
        recalculate.add(mainPointer);
        references.add(cellPointer);
    }

    private void tableModelChanged(TableModelEvent e) {
        if (model.getRowCount() > subscribed.rowCount()) {
            subscribed.addRow();
            references.addRow();
        } else if (model.getRowCount() < subscribed.rowCount()) {
            subscribed.removeRow();
            references.removeRow();
        }

        if (model.getColumnCount() > subscribed.columnCount()) {
            subscribed.addColumn();
            references.addColumn();
        } else if (model.getColumnCount() < subscribed.columnCount()) {
            subscribed.removeColumn();
            references.removeColumn();
        }
    }
}
