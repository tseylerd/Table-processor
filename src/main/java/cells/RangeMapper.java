package cells;

import java.util.*;

/**
 * Base class of mapper between ranges and values.
 * Used for fast access to range parameters
 * <b>This mapper allow as to store and get some range parameters in fast way for a big tables
 * (at least 30000 * 30000} </b>
 * @author Dmitriy Tseyler
 * @param <T> a value class parameter
 */
public abstract class RangeMapper<T> {

    private Map<CellRange, T> map;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private CellRange fullRange;

    RangeMapper() {
        map = new HashMap<>();
    }

    public void setMap(Map<CellRange, T> map) {
        if (map.isEmpty()) {
            return;
        }

        this.map = map;
        reset();
        map.keySet().forEach(this::indexXY);
        createNewRange();
    }

    private void reset() {
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;
        fullRange = null;
    }

    private void createNewRange() {
        if (minX == Integer.MAX_VALUE) {
            return;
        }

        fullRange = new CellRange(minY, minX, maxY, maxX);
    }

    public void delete(CellRange range) {
        map.remove(range);
        reset();
        Map<CellRange, T> newMap = new HashMap<>();
        for (Map.Entry<CellRange, T> cellRangeListEntry : map.entrySet()) {
            CellRange existing = cellRangeListEntry.getKey();
            T existingValue = cellRangeListEntry.getValue();

            List<CellRange> ranges = existing.split(range);
            if (ranges == null || ranges.isEmpty()) {
                continue;
            }
            for (CellRange cellRange : ranges) {
                newMap.put(cellRange, existingValue);
                indexXY(cellRange);
            }
        }
        map = newMap;
        createNewRange();
    }

    /**
     * Setter for range value
     * We iterate over all saved ranges and watch, if range intersects or contains new range.
     * If range, exists, we simply replace it's value in new map.
     * Else, if range intersects with existing ranges, we split existing ranges and put splitted ranges in new map
     * with old values.
     * @param range
     * Range for value
     * @param newValue
     * Value for range
     */
    public void set(CellRange range, T newValue) {
        Map<CellRange, T> newMap = new HashMap<>();
        reset();
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
        createNewRange();
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

    /**
     * Getter for range value.
     * First we check that it is possible that the mapper contains interested range.
     * If not, we returns default value.
     * If possible, we check that range contains in full modified range.
     * If not, we split interested range by full range and concatenate splitter value with splitted parts.
     * If splitting is not possible, we return the default value.
     * If full modified range contains our range, we check mapper on containing our range.
     * If mapper contains range, we return it's value. Else we iterate over all ranges and watch for intersections.
     * If intersections found, we concatenate all splitted ranges values and return result.
     * @param range
     * Range to find
     * @return value for range
     */
    public T get(CellRange range) {
        if (fullRange == null) {
            return defaultValue();
        }
        if (range == null) {
            return null;
        }
        if (!fullRange.isInside(range)) {
            SplittedRange splitted = range.splitHonestly(fullRange);
            if (splitted == null || !splitted.splitSuccessful()) {
                return defaultValue();
            } else {
                return concatenate(splitted, range);
            }
        } else {
            T existingValues = map.get(range);
            if (existingValues == null) {
                return rangeNotFound(range);
            } else {
                return existingValues;
            }
        }
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
            if (splittedRange != null && splittedRange.splitSuccessful()) {
                return processSplitted(splittedRange, cellRangeListEntry.getValue());
            }
        }
        return defaultValue();
    }

    public Map<CellRange, T> getMap() {
        return Collections.unmodifiableMap(map);
    }

    abstract T processSplitted(SplittedRange splittedRange, T existing);
    abstract T defaultValue();
    abstract T concatenate(SplittedRange splittedRange, CellRange range);
}