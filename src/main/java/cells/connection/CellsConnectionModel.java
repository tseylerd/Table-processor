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

    private void clear(PointerNode node) {
        //noinspection unchecked
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
        clear(pointer);
        this.references.set(pointer.getRow(), pointer.getColumn(), ranges);
        processRanges(pointer, ranges);
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
        //noinspection unchecked
        List<PointerNode> recalculate = subscribed.get(row, column);
        if (recalculate == null) {
            recalculate = new LinkedList<>();
            subscribed.set(cellPointer.getPointer().getRow(), cellPointer.getPointer().getColumn(), recalculate);
        }
        recalculate.add(mainPointer);
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
