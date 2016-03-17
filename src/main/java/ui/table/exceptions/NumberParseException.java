package ui.table.exceptions;

import ui.table.error.Error;

/**
 * Exception, when we parsing string as double value, but it just a string
 * @author Dmitriy Tseyler
 */
public class NumberParseException extends SpreadSheetException {
    public NumberParseException(String value) {
        super(Error.NUMBER_FORMAT, 4, value);
    }
}
