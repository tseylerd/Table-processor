package ui.table;

import ui.util.CellPointer;

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

    public void subscribe(CellPointer pointer, CellPointer... referenced) {
        Set<CellPointer> references = this.references.get(pointer);
        if (references != null) {
            for (CellPointer reference : references) {
                toRecalculate.get(reference).remove(pointer);
            }
        }

        Set<CellPointer> newReferences = new HashSet<>();
        for (CellPointer cellPointer : referenced) {
            Set<CellPointer> pointers = toRecalculate.get(cellPointer);
            if (pointers == null) {
                pointers = new HashSet<>();
                toRecalculate.put(cellPointer, pointers);
            }
            pointers.add(pointer);
            newReferences.add(cellPointer);
        }
        this.references.put(pointer, newReferences);
    }
}
