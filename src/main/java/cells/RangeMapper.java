package cells;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RangeMapper<T> {
    private static final int MIN = 225;

    private Map<CellRange, T> map;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private CellRange fullRange;

    protected RangeMapper() {
        map = new HashMap<>();
    }

    public void set(CellRange range, T newValue) {
        Map<CellRange, T> newMap = new HashMap<>();
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;
        for (Map.Entry<CellRange, T> cellRangeListEntry : map.entrySet()) {
            CellRange existing = cellRangeListEntry.getKey();
            T existingValue = cellRangeListEntry.getValue();

            List<CellRange> ranges = existing.split(range);
            if (ranges == null) {
                newMap.put(existing, existingValue);
                indexXY(existing);
                continue;
            }
            if (ranges.isEmpty()) {
                continue;
            }
            for (CellRange cellRange : ranges) {
                newMap.put(cellRange, existingValue);
                indexXY(cellRange);
            }
        }
        newMap.put(range, newValue);
        indexXY(range);
        map = newMap;
        fullRange = new CellRange(minY, minX, maxY, maxX);
    }

    private void indexXY(CellRange range) {
        if (range.getFirstRow() < minY) {
            minY = range.getFirstRow();
        }
        if (range.getLastRow() > maxY) {
            maxY = range.getLastRow();
        }
        if (range.getLastColumn() > maxX) {
            maxX = range.getLastColumn();
        }
        if (range.getFirstColumn() < minX) {
            minX = range.getFirstColumn();
        }
    }

    public T get(CellRange range) {
        if (fullRange == null) {
            return defaultValue(range);
        }
        if (range == null) {
            return null;
        }
        if (!fullRange.isInside(range)) {
            SplittedRange splitted = range.splitHonestly(fullRange);
            if (splitted == null || !splitted.splitSucceful()) {
                return defaultValue(range);
            } else {
                concatenate(splitted, range);
            }
        } else {
            T existingValues = map.get(range);
            if (existingValues == null) {
                return rangeNotFound(range);
            } else {
                return existingValues;
            }
        }
        return defaultValue(range);
    }

     private T rangeNotFound(CellRange range) {
        for (Map.Entry<CellRange, T> cellRangeListEntry : map.entrySet()) {
            CellRange existing = cellRangeListEntry.getKey();
            if (existing.isInside(range))
                return cellRangeListEntry.getValue();
        }
        for (Map.Entry<CellRange, T> cellRangeListEntry : map.entrySet()) {
            CellRange existing = cellRangeListEntry.getKey();
            SplittedRange splittedRange = range.splitHonestly(existing);
            if (splittedRange.splitSucceful()) {
                return processSplitted(splittedRange, existing, map.get(existing));
            }
        }
        return defaultValue(range);
    }


    abstract T processSplitted(SplittedRange splittedRange, CellRange range, T existing);
    abstract T defaultValue(CellRange range);
    abstract T concatenate(SplittedRange splittedRange, CellRange range);
    abstract T processSmallRange(CellRange range);
}