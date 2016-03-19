package cells;

import cells.pointer.CellPointer;

import java.awt.*;

/**
 * Mapper implementation for colors.
 * @author Dmitriy Tseyler
 */

public class ColorMapper extends RangeMapper<Color> {
    private final Color defaultColor;

    public ColorMapper(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    @Override
    Color defaultValue() {
        return defaultColor;
    }

    /**
     * We returns not default color only if all splitted ranges colors are equals
     */
    @Override
    Color processSplitted(SplittedRange splittedRange, Color existing) {
        Color leftColor = get(splittedRange.getLeft());
        Color rightColor = get(splittedRange.getRight());
        Color upColor = get(splittedRange.getUp());
        Color downColor = get(splittedRange.getDown());
        if (leftColor != null && !leftColor.equals(existing)) {
            return defaultColor;
        }
        if (rightColor != null && !rightColor.equals(existing)) {
            return defaultColor;
        }
        if (upColor != null && !upColor.equals(existing)) {
            return defaultColor;
        }
        if (downColor != null && !downColor.equals(existing)) {
            return defaultColor;
        }
        return existing;
    }

    @Override
    Color concatenate(SplittedRange splittedRange, CellRange range) {
        return defaultColor;
    }

    public Color getColor(CellPointer pointer) {
        return get(new CellRange(pointer, pointer));
    }
}
