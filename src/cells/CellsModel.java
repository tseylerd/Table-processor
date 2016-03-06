package cells;

import cells.CellPointer;
import cells.CellRange;
import ui.table.SpreadSheetModel;

import java.util.*;

/**
 * @author Dmitriy Tseyler
 */
public class CellsModel {
    private final HashMap<CellPointer, Set<CellPointer>> toRecalculate;
    private final HashMap<CellPointer, Set<CellPointer>> references;

    private final SpreadSheetModel model;

    public CellsModel(SpreadSheetModel model) {
        toRecalculate = new HashMap<>();
        references = new HashMap<>();
        this.model = model;
    }

    public void cellChanged(CellPointer pointer) {
        Set<CellPointer> refs = toRecalculate.get(pointer);
        if (refs == null)
            return;

        for (CellPointer ref : refs) {
            model.recalculate(ref);
        }
    }

    public void subscribe(CellPointer pointer, CellRange... ranges) {
        clearOldReferences(pointer);
        HashSet<CellPointer> newReferences = new HashSet<>();
        for (CellRange range : ranges) {
            processRange(pointer, range, newReferences);
        }
        references.put(pointer, newReferences);
    }

    private void processRange(CellPointer pointer, CellRange range, Set<CellPointer> newRefs) { // // TODO: 06.03.16 Store only ranges in newRefs
        for (CellPointer cellPointer : range) {
            processReference(pointer, cellPointer, newRefs);
        }
    }

    private void clearOldReferences(CellPointer pointer) {
        Set<CellPointer> references = this.references.get(pointer);
        if (references != null) {
            for (CellPointer reference : references) {
                toRecalculate.get(reference).remove(pointer);
            }
        }
    }

    public void subscribe(CellPointer pointer, CellPointer... referenced) { //// TODO: 06.03.16 Problem if ranges and cell pointers are exists in expression
        clearOldReferences(pointer);
        Set<CellPointer> newReferences = new HashSet<>();
        for (CellPointer cellPointer : referenced) {
            processReference(pointer, cellPointer, newReferences);
        }
        this.references.put(pointer, newReferences);
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
