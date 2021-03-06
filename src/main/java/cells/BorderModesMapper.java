package cells;

import cells.pointer.CellPointer;

/**
 * Mapper implementation for border modes.
 * @author Dmitriy Tseyler
 */
public class BorderModesMapper extends RangeMapper<Boolean> {
    @Override
    Boolean concatenate(SplittedRange splittedRange, CellRange range) {
        Boolean need = get(splittedRange.getSplitter());
        Boolean result = defaultValue();
        return result && need;
    }

    @Override
    Boolean defaultValue() {
        return true;
    }

    /**
     * Returns not existing value only if all splitted range values are the same
     */
    @Override
    Boolean processSplitted(SplittedRange splittedRange, Boolean existing) {
        Boolean leftModes = get(splittedRange.getLeft());
        Boolean rightModes = get(splittedRange.getRight());
        Boolean upModes = get(splittedRange.getUp());
        Boolean downModes = get(splittedRange.getDown());

        existing &= leftModes == null || leftModes;
        existing &= rightModes == null || rightModes;
        existing &= upModes == null || upModes;
        existing &= downModes == null || downModes;
        return existing;
    }

    public Boolean need(CellPointer pointer) {
        return get(new CellRange(pointer, pointer));
    }
}
