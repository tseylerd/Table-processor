package cells;

import ui.laf.grid.BorderMode;
import ui.laf.grid.TableColorModel;
import ui.table.SpreadSheetModel;

import java.util.*;

/**
 * @author Dmitriy Tseyler
 */
public class RangeAggregator {
    private Map<CellRange, List<BorderMode>> ranges;
    private int summarySize;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private final SpreadSheetModel model;
    private final TableColorModel colorModel;

    public RangeAggregator(SpreadSheetModel model, TableColorModel colorModel) {
        ranges = new HashMap<>();
        this.model = model;
        this.colorModel = colorModel;
    }

    public void setBorderMode(CellRange range, List<BorderMode> newModes) {
        Map<CellRange, List<BorderMode>> newMap = new HashMap<>();
        summarySize = 0;
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;
        for (Map.Entry<CellRange, List<BorderMode>> cellRangeListEntry : ranges.entrySet()) {
            CellRange existing = cellRangeListEntry.getKey();
            List<BorderMode> modes = cellRangeListEntry.getValue();

            List<CellRange> ranges = existing.split(range);
            if (ranges == null) {
                newMap.put(existing, modes);
                indexXY(existing);
                continue;
            }
            if (ranges.isEmpty()) {
                continue;
            }
            for (CellRange cellRange : ranges) {
                newMap.put(cellRange, modes);
                indexXY(cellRange);
            }
        }
        newMap.put(range, newModes);
        indexXY(range);
        ranges = newMap;
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
        summarySize += range.size();
    }
    public List<BorderMode> getModes(CellRange range) {
        List<BorderMode> modes = ranges.get(range);
        if (modes == null) {
            for (Map.Entry<CellRange, List<BorderMode>> cellRangeListEntry : ranges.entrySet()) {
                CellRange existing = cellRangeListEntry.getKey();
                if (existing.isInside(range))
                    return cellRangeListEntry.getValue();
            }
        } else {
            return modes;
        }
        modes = new ArrayList<>();

        for (BorderMode borderMode : BorderMode.values()) {
            if (range == null || range.size() > summarySize) {
                if (borderMode.isModeEnabled(model, range)) {
                    modes.add(borderMode);
                }
            } else {
                if (borderMode.isModeTurnedOn(colorModel, range)) {
                    modes.add(borderMode);
                }
            }
        }
        return Collections.unmodifiableList(modes);
    }
}
