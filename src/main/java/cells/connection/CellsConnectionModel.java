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
 * <b>Now, this model is the bottleneck of application. It hangs on ranges of about 3000x3000 and bigger.
 * In future, this model should use something like {@link cells.RangeMapper}.</b>
 * @author Dmitriy Tseyler
 */
public class CellsConnectionModel {
    private static final int DEFAULT_CAPACITY = 15;

    private final LazyDynamicArray<List<PointerNode>> subscribed; // array of cells, that are subscribed on cell (i, j) changes
    private final LazyDynamicArray<List<CellRange>> references; // array of referenced by cell (i, j) ranges

    private final LinkedList<PointerNode> topSortList;
    private final Set<PointerNode> cyclic;
    private final SpreadSheetModel model;

    private PointerNode firstInCycle;

    @SuppressWarnings("unchecked")
    public CellsConnectionModel(SpreadSheetModel model) {
        subscribed = new LazyDynamicArray<>(model.getRowCount(), model.getColumnCount(), (Class<List<PointerNode>>)(Object)List.class);
        references = new LazyDynamicArray<>(model.getRowCount(), model.getColumnCount(), (Class<List<CellRange>>)(Object)List.class);
        topSortList = new LinkedList<>();
        cyclic = new HashSet<>();

        this.model = model;

        model.addTableModelListener(this::tableModelChanged);
    }

    /**
     * Build list of top sorted nodes
     * Bulid list of cycle part nodes
     */
    private void dfs(PointerNode node, Set<PointerNode> inThisWay) {
        if (node.isVisited()) {
            indexCyclicReference(node);
        }
        if (inThisWay.contains(node)) {
            return;
        }

        List<PointerNode> nodes = subscribed.get(node.getRow(), node.getColumn());

        if (nodes == null) {
            topSortList.addLast(node);
            return;
        }

        inThisWay.add(node);
        node.setVisited(true);
        for (PointerNode current : nodes) {
            try {
                dfs(current, inThisWay);
            } catch (CyclicReferenceException e) {
                indexCyclicReference(node);
            }
        }
        node.setVisited(false);

        if (!cyclic.contains(node)) {
            topSortList.addLast(node);
        }
    }

    private void indexCyclicReference(PointerNode node) {
        cyclic.add(node);

        if (firstInCycle == null) {
            firstInCycle = node;
            node.setVisited(false);
            throw new CyclicReferenceException();
        }

        if (!node.equals(firstInCycle)) {
            node.setVisited(false);
            throw new CyclicReferenceException();
        } else {
            firstInCycle = null;
        }
    }

    /**
     * Here we try recalculate all subscribed cells
     * In reverse order from topological sort
     */
    public void cellChanged(PointerNode pointer) throws CyclicReferenceException {
        dfs(pointer, new HashSet<>());
        while (!topSortList.isEmpty()) {
            model.recalculate(topSortList.pollLast().getPointer());
        }
        for (PointerNode pointerNode : cyclic) {
            model.setCyclicReference(pointerNode.getPointer());
        }
        topSortList.clear();
        cyclic.clear();
    }

    private void clear(PointerNode node) {
        List<CellRange> refs = references.get(node.getRow(), node.getColumn());
        if (refs == null) {
            return;
        }

        for (CellRange ref : refs) {
            for (CellPointer pointer : ref) {
                List<PointerNode> pointerNodes = subscribed.get(pointer.getRow(), pointer.getColumn());
                if (pointerNodes != null) {
                    pointerNodes.remove(node);
                }
            }
        }
    }

    public void subscribe(PointerNode pointer, List<CellRange> ranges) {
        if (pointer.getRow() >= references.rowCount() || pointer.getColumn() >= references.columnCount()) {
            return;
        }
        clear(pointer); // clear old subscribtion
        this.references.set(pointer.getRow(), pointer.getColumn(), ranges);
        processRanges(pointer, ranges); // write new subscription
    }

    private void processRanges(PointerNode pointer, List<CellRange> ranges) {
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
        if (row >= subscribed.rowCount() || column >= subscribed.columnCount()) {
            return;
        }

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
