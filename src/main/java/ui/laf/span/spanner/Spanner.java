package ui.laf.span.spanner;

import cells.CellRange;
import ui.table.SpreadSheetModel;

/**
 * @author Dmitriy Tseyler
 */
public interface Spanner {
    void span(SpreadSheetModel model, CellRange from, CellRange to);
}
