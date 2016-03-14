package ui.table.exceptions;

import ui.table.error.Error;

/**
 * @author Dmitriy Tseyler
 */
public class InvalidCellPointerException extends SpreadSheetException {
    public InvalidCellPointerException() {
        super(Error.INDEX_OUT_OF_RANGE, 2, "");
    }
}
