package ui.table.exceptions;

import ui.table.error.Error;

/**
 * @author Dmitriy Tseyler
 */
public class NumberParseException extends SpreadSheetException {
    public NumberParseException(String value) {
        super(Error.NUMBER_FORMAT, 4, value);
    }
}
