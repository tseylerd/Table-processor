package ui.table;

import ui.util.CellPointer;

import java.util.*;

/**
 * @author Dmitriy Tseyler
 */
public class CellsModel {
    private final HashMap<CellPointer, Set<CellPointer>> pointerMap;
    private final SpreadSheetModel model;

    public CellsModel(SpreadSheetModel model) {
        pointerMap = new HashMap<>();
        this.model = model;
    }

    public void cellChanged(CellPointer pointer) {
        Set<CellPointer> refs = pointerMap.get(pointer);
        if (refs == null)
            return;
        
        for (CellPointer ref : refs) {
            model.recalculate(ref);
        }
    }

    public void subscribe(CellPointer pointer, CellPointer... referenced) {
        for (CellPointer cellPointer : referenced) {
            Set<CellPointer> pointers = pointerMap.get(cellPointer);
            if (pointers == null) {
                pointers = new HashSet<>();
                pointerMap.put(cellPointer, pointers);
            }
            pointers.add(pointer);
        }
    }
}
