package cells.iterator;

/**
 * @author Dmitriy Tseyler
 */
public interface IntBiChecker<T> {
    boolean accept(T object, int value);
}
