package ui.table.exceptions;

import ui.table.error.Error;

/**
 * When row or column in CellPointer is lower than 0
 * @author Dmitriy Tseyler
 */
public class InvalidCellPointerException extends SpreadSheetException {
    public InvalidCellPointerException() {
        super(Error.INDEX_OUT_OF_RANGE, 2, "");
    }
}
