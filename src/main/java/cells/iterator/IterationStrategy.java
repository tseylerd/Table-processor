package cells.iterator;

/**
 * @author Dmitriy Tseyler
 */
public interface IterationStrategy<T, U extends AbstractSpreadSheetIterator> {
    T nextOf(T value, U iterator);
}
