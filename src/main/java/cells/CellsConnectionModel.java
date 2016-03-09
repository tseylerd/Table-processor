package cells;

import ui.table.SpreadSheetModel;
import ui.table.exceptions.CyclicReferenceException;

import java.util.*;

/**
 * @author Dmitriy Tseyler
 */
public class CellsConnectionModel {
    private final HashMap<CellPointer, Set<CellPointer>> toRecalculate;
    private final HashMap<CellPointer, Set<CellPointer>> references;

    private final HashSet<CellPointer> visited;
    private final SpreadSheetModel model;

    private boolean cycle;

    public CellsConnectionModel(SpreadSheetModel model) {
        toRecalculate = new HashMap<>();
        references = new HashMap<>();
        visited = new HashSet<>();
        this.model = model;
    }

    public void cellChanged(CellPointer pointer) throws CyclicReferenceException {
        if (toRecalculate.get(pointer) == null) {
            return;
        }

        if (visited.contains(pointer)) {
            cycle = true;
            throw new CyclicReferenceException();
        }

        visited.add(pointer);
        Set<CellPointer> refs = new HashSet<>(toRecalculate.get(pointer));
        refs.forEach(model::recalculate);
        visited.remove(pointer);

        if (cycle) {
            throw new CyclicReferenceException();
        }
    }


    public void resetErrors() {
        cycle = false;
    }

    private void clearOldReferences(CellPointer pointer) { //// TODO: 06.03.16 more effective (store only ranges for ranges, not clearing references that we should add later)
        Set<CellPointer> references = this.references.get(pointer);
        if (references != null) {
            for (CellPointer reference : references) {
                toRecalculate.get(reference).remove(pointer);
            }
        }
    }

    public void subscribe(CellPointer pointer, Set<CellPointer> referenced, Set<CellRange> ranges) {
        clearOldReferences(pointer);
        Set<CellPointer> newRefs = new HashSet<>();
        processCellPointers(pointer, referenced, newRefs);
        processRanges(pointer, ranges, newRefs);
        references.put(pointer, newRefs);
    }

    public void processRanges(CellPointer pointer, Set<CellRange> ranges, Set<CellPointer> newReferences) {
        for (CellRange range : ranges) {
            processRange(pointer, range, newReferences);
        }
    }

    private void processRange(CellPointer pointer, CellRange range, Set<CellPointer> newRefs) {
        for (CellPointer cellPointer : range) {
            processReference(pointer, cellPointer, newRefs);
        }
    }

    private void processCellPointers(CellPointer pointer, Set<CellPointer> referenced, Set<CellPointer> newReferences) {
        for (CellPointer cellPointer : referenced) {
            processReference(pointer, cellPointer, newReferences);
        }
    }

    private void processReference(CellPointer mainPointer, CellPointer cellPointer, Set<CellPointer> references) {
        Set<CellPointer> recalculate = toRecalculate.get(cellPointer);
        if (recalculate == null) {
            recalculate = new HashSet<>();
            toRecalculate.put(cellPointer, recalculate);
        }
        recalculate.add(mainPointer);
        references.add(cellPointer);
    }
}
