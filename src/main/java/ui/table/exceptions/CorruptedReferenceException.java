package ui.table.exceptions;

import ui.table.error.Error;

/**
 * @author Dmitriy Tseyler
 */
public class CorruptedReferenceException extends SpreadSheetException {
    public CorruptedReferenceException() {
        super(Error.BAD_REFERENCE, 5, "");
    }
}
