package ui.table.exceptions;

import ui.table.error.Error;

/**
 * This exception indicate about reference on empty cell. In this case, we do not see if anything in the cell contains.
 * @author Dmitriy Tseyler
 */
public class EmptyValueException extends SpreadSheetException {
    public EmptyValueException() {
        super(Error.EMPTY_VALUE, 5, "");
    }
}
