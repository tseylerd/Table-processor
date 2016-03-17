package ui.table.exceptions;

import ui.table.error.Error;

/**
 * Indicates about cycles in references in table
 * @author Dmitriy Tseyler
 */
public class CyclicReferenceException extends SpreadSheetException {
    public CyclicReferenceException() {
        super(Error.CYCLIC_REFERENCE, 3, "");
    }
}
