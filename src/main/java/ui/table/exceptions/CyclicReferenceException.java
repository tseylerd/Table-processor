package ui.table.exceptions;

import ui.table.error.Error;

/**
 * @author Dmitriy Tseyler
 */
public class CyclicReferenceException extends SpreadSheetException {
    public CyclicReferenceException() {
        super(Error.CYCLIC_REFERENCE, 3, "");
    }
}
