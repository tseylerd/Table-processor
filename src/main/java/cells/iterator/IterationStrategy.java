package cells.iterator;

/**
 * Base interfase of iteration strategy used in {@link AbstractSpreadSheetIterator}
 * @author Dmitriy Tseyler
 */
public interface IterationStrategy<T, U extends AbstractSpreadSheetIterator> {
    T nextOf(T value, U iterator);
}
