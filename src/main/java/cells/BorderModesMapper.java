package cells;

import cells.pointer.CellPointer;
import com.sun.org.apache.xpath.internal.operations.Bool;
import ui.laf.grid.BorderMode;
import ui.laf.grid.TableColorModel;
import ui.table.SpreadSheetModel;

import java.util.*;

/**
 * @author Dmitriy Tseyler
 */
public class BorderModesMapper extends RangeMapper<Boolean> {
    public static final int RIGHT_INDEX = 0;
    public static final int DOWN_INDEX = 1;

    private final SpreadSheetModel model;
    private final TableColorModel colorModel;

    public BorderModesMapper(SpreadSheetModel model, TableColorModel colorModel) {
        this.model = model;
        this.colorModel = colorModel;
    }

    @Override
    Boolean concatenate(SplittedRange splittedRange, CellRange range) {
        Boolean need = get(splittedRange.getSplitter());
        Boolean result = defaultValue(range);
        return result && need;
    }

    @Override
    Boolean defaultValue(CellRange range) {
        return true;
    }

    @Override
    Boolean processSmallRange(CellRange range) {
        return  defaultValue(range);
    }

    @Override
    Boolean processSplitted(SplittedRange splittedRange, CellRange range, Boolean existing) {
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
