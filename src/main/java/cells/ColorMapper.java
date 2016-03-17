package cells;

import cells.pointer.CellPointer;

import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */

public class ColorMapper extends RangeMapper<Color> {
    private final Color defaultColor;

    public ColorMapper(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    @Override
    Color defaultValue(CellRange range) {
        return defaultColor;
    }

    @Override
    Color processSplitted(SplittedRange splittedRange, CellRange range, Color existing) {
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
