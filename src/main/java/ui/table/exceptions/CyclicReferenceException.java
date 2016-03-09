package ui.table.exceptions;

/**
 * @author Dmitriy Tseyler
 */
public class CyclicReferenceException extends Exception {
    public CyclicReferenceException() {
        super("Cell can't references on itself");
    }
}
