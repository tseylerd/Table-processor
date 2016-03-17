package ui.table.exceptions;

import ui.table.error.Error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base exceptions class.
 * In some cases, I want to show the same highest priority exception in not depending on the order of errors
 * @see {@link math.calculator.expression.BinaryExpression}
 * @author Dmitriy Tseyler
 */
public class SpreadSheetException extends RuntimeException implements Comparable<SpreadSheetException> {
    private final Error error;
    private final int priority;
    private final String value;

    public SpreadSheetException(Error error, int priority, String value) {
        this.error = error;
        this.priority = priority;
        this.value = value;
    }

    public Error getError() {
        return error;
    }

    public static void throwIfNeeded(SpreadSheetException... exceptions) {
        List<SpreadSheetException> exceptionList = new ArrayList<>();
        for (SpreadSheetException exception : exceptions) {
            if (exception != null) {
                exceptionList.add(exception);
            }
        }
        if (exceptionList.isEmpty()) {
            return;
        }
        Collections.sort(exceptionList);
        throw exceptionList.get(0);
    }

    @Override
    public int compareTo(SpreadSheetException o) {
        if (o.priority > priority) {
            return -1;
        } else if (o.priority < priority) {
            return 1;
        }
        return 0;
    }

    public String getValue() {
        return value;
    }
}
