package cells.connection;

import cells.CellRange;
import cells.pointer.CellPointer;
import storage.LazyDynamicArray;
import ui.table.SpreadSheetModel;
import ui.table.exceptions.CyclicReferenceException;

import javax.swing.event.TableModelEvent;
import java.util.*;

/**
 * This model represents a graph of table cells. It knows, which cells we should be recalculated on cell change.
 * <b>Now, this model is the bottleneck of apllication. It hangs on ranges of about 3000x3000 and bigger.
 * In future, this model should use something like {@link cells.RangeMapper}.</b>
 * @author Dmitriy Tseyler
 */
public class CellsConnectionModel {
    private static final int DEFAULT_CAPACITY = 15;

    private final LazyDynamicArray<List<PointerNode>> subscribed; // array of cells, that are subscribed on cell (i, j) changes
    private final LazyDynamicArray<List<CellRange>> references; // array of referenced by cell (i, j) ranges

    private final SpreadSheetModel model;

    private boolean cycle;

    @SuppressWarnings("unchecked")
    public CellsConnectionModel(SpreadSheetModel model) {
        subscribed = new LazyDynamicArray<>(model.getRowCount(), model.getColumnCount(), (Class<List<PointerNode>>)(Object)List.class);
        references = new LazyDynamicArray<>(model.getRowCount(), model.getColumnCount(), (Class<List<CellRange>>)(Object)List.class);
        this.model = model;
        model.addTableModelListener(this::tableModelChanged);
    }

    /**
     * Here we try recalculate all subscribed cells
     */
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

        try {
            pointer.setVisited(true);
            List<PointerNode> refs = subscribed.get(row, column);
            refs.forEach(model::recalculate);
        } finally {
            pointer.setVisited(false);
        }

        if (cycle) {
            throw new CyclicReferenceException();
        }
    }


    public void resetErrors() {
        cycle = false;
    }

    private void clear(PointerNode node) {
        List<CellRange> refs = references.get(node.getRow(), node.getColumn());
        if (refs == null) {
            return;
        }

        for (CellRange ref : refs) {
            for (CellPointer pointer : ref) {
                subscribed.get(pointer.getRow(), pointer.getColumn()).remove(node);
            }
        }
    }

    public void subscribe(PointerNode pointer, List<CellRange> ranges) {
        clear(pointer); // clear old subscribtion
        this.references.set(pointer.getRow(), pointer.getColumn(), ranges);
        processRanges(pointer, ranges); // write new subscription
    }

    public void processRanges(PointerNode pointer, List<CellRange> ranges) {
        for (CellRange range : ranges) {
            processRange(pointer, range);
        }
    }

    private void processRange(PointerNode pointer, CellRange range) {
        for (CellPointer cellPointer : range) {
            processReference(pointer, new PointerNode(cellPointer));
        }
    }

    private void processReference(PointerNode mainPointer, PointerNode cellPointer) {
        int row = cellPointer.getRow();
        int column = cellPointer.getColumn();
        List<PointerNode> recalculate = subscribed.get(row, column);
        if (recalculate == null) {
            recalculate = new ArrayList<>(DEFAULT_CAPACITY);
            subscribed.set(cellPointer.getPointer().getRow(), cellPointer.getPointer().getColumn(), recalculate);
        }
        recalculate.add(mainPointer);
    }

    /**
     * Keep arrays in actual state
     */
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
