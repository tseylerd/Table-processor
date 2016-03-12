package cells.connection;

import cells.CellRange;
import cells.pointer.CellPointer;
import ui.table.SpreadSheetModel;
import ui.table.exceptions.CyclicReferenceException;

import java.util.*;

/**
 * @author Dmitriy Tseyler
 */
public class CellsConnectionModel {
    private final Map<PointerNode, Set<PointerNode>> toRecalculate;
    private final Map<PointerNode, Set<PointerNode>> references;

    private final SpreadSheetModel model;

    private boolean cycle;

    public CellsConnectionModel(SpreadSheetModel model) {
        toRecalculate = new HashMap<>();
        references = new HashMap<>();
        this.model = model;
    }

    public void cellChanged(PointerNode pointer) throws CyclicReferenceException {
        if (toRecalculate.get(pointer) == null) {
            return;
        }

        if (pointer.isVisited()) {
            cycle = true;
            throw new CyclicReferenceException();
        }

        pointer.setVisited(true);
        Set<PointerNode> refs = new HashSet<>(toRecalculate.get(pointer));
        refs.forEach(model::recalculate);
        pointer.setVisited(false);

        if (cycle) {
            throw new CyclicReferenceException();
        }
    }


    public void resetErrors() {
        cycle = false;
    }

    private void clearOldReferences(PointerNode pointer) { //// TODO: 06.03.16 more effective (store only ranges for ranges, not clearing references that we should add later)
        Set<PointerNode> references = this.references.get(pointer);
        if (references != null) {
            for (PointerNode reference : references) {
                toRecalculate.get(reference).remove(pointer);
            }
        }
    }

    public void subscribe(CellPointer cellPointer, Set<CellPointer> referenced, Set<CellRange> ranges) {
        PointerNode pointer = new PointerNode(cellPointer);
        clearOldReferences(pointer);
        Set<PointerNode> newRefs = new HashSet<>();
        processCellPointers(pointer, referenced, newRefs);
        processRanges(pointer, ranges, newRefs);
        references.put(pointer, newRefs);
    }

    public void processRanges(PointerNode pointer, Set<CellRange> ranges, Set<PointerNode> newReferences) {
        for (CellRange range : ranges) {
            processRange(pointer, range, newReferences);
        }
    }

    private void processRange(PointerNode pointer, CellRange range, Set<PointerNode> newRefs) {
        for (CellPointer cellPointer : range) {
            processReference(pointer, new PointerNode(cellPointer), newRefs);
        }
    }

    private void processCellPointers(PointerNode pointer, Set<CellPointer> referenced, Set<PointerNode> newReferences) {
        for (CellPointer cellPointer : referenced) {
            processReference(pointer, new PointerNode(cellPointer), newReferences);
        }
    }

    private void processReference(PointerNode mainPointer, PointerNode cellPointer, Set<PointerNode> references) {
        Set<PointerNode> recalculate = toRecalculate.get(cellPointer);
        if (recalculate == null) {
            recalculate = new HashSet<>();
            toRecalculate.put(cellPointer, recalculate);
        }
        recalculate.add(mainPointer);
        references.add(cellPointer);
    }
}
