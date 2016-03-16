package cells;

import ui.laf.grid.BorderMode;
import ui.laf.grid.TableColorModel;
import ui.table.SpreadSheetModel;

import java.util.*;

/**
 * @author Dmitriy Tseyler
 */
public class BorderModesMapper extends RangeMapper<List<BorderMode>> {
    private final SpreadSheetModel model;
    private final TableColorModel colorModel;

    public BorderModesMapper(SpreadSheetModel model, TableColorModel colorModel) {
        this.model = model;
        this.colorModel = colorModel;
    }

    @Override
    List<BorderMode> concatenate(SplittedRange splittedRange, CellRange range) {
        List<BorderMode> modes = get(splittedRange.getSplitter());
        List<BorderMode> result = defaultValue(range);

        if (splittedRange.getLeft() != null) {
            if (!modes.contains(BorderMode.LEFT)) {
                result.remove(BorderMode.LEFT);
            }
        }
        if (splittedRange.getRight() != null) {
            if (!modes.contains(BorderMode.RIGHT)) {
                result.remove(BorderMode.RIGHT);
            }
        }
        if (splittedRange.getUp() != null) {
            if (!modes.contains(BorderMode.UP)) {
                result.remove(BorderMode.UP);
            }
        }
        if (splittedRange.getDown() != null) {
            if (!modes.contains(BorderMode.DOWN)) {
                result.remove(BorderMode.DOWN);
            }
        }
        if (!modes.contains(BorderMode.ALL_LINES)) {
            result.remove(BorderMode.ALL_LINES);
        }
        return result;
    }

    @Override
    List<BorderMode> defaultValue(CellRange range) {
        List<BorderMode> modes = new ArrayList<>();
        for (BorderMode mode : BorderMode.values()) {
            if (mode.isModeEnabled(model, range)) {
                modes.add(mode);
            }
        }
        return modes;
    }

    @Override
    List<BorderMode> processSmallRange(CellRange range) {
        List<BorderMode> modes = new ArrayList<>();
        for (BorderMode mode : BorderMode.values()) {
            if (mode.isModeEnabled(model, range) && mode.isModeTurnedOn(colorModel, range)) {
                modes.add(mode);
            }
        }
        return modes;
    }

    @Override
    List<BorderMode> processSplitted(SplittedRange splittedRange, CellRange range, List<BorderMode> existing) {
        List<BorderMode> leftModes = get(splittedRange.getLeft());
        List<BorderMode> rightModes = get(splittedRange.getRight());
        List<BorderMode> upModes = get(splittedRange.getUp());
        List<BorderMode> downModes = get(splittedRange.getDown());
        List<BorderMode> modes = new ArrayList<>();

        processSideMode(BorderMode.LEFT, existing, leftModes, modes);
        processSideMode(BorderMode.RIGHT, existing, rightModes, modes);
        processSideMode(BorderMode.UP, existing, upModes, modes);
        processSideMode(BorderMode.DOWN, existing, downModes, modes);
        boolean leftContainsGrid = leftModes == null || leftModes.contains(BorderMode.ALL_LINES);
        boolean downContainsGrid = downModes == null || downModes.contains(BorderMode.ALL_LINES);
        boolean upContainsGrid = upModes == null || upModes.contains(BorderMode.ALL_LINES);
        boolean rightContainsGrid = rightModes == null || rightModes.contains(BorderMode.ALL_LINES);
        boolean existingContainsGrid = existing.contains(BorderMode.ALL_LINES);
        if (leftContainsGrid && rightContainsGrid && upContainsGrid && downContainsGrid && existingContainsGrid) {
            modes.add(BorderMode.ALL_LINES);
        }
        return modes;
    }

    private void processSideMode(BorderMode mode, List<BorderMode> existing, List<BorderMode> splittedPart, List<BorderMode> target) {
        if (splittedPart != null && splittedPart.contains(mode) && !existing.contains(mode)) {
            target.add(mode);
        }
    }
}
