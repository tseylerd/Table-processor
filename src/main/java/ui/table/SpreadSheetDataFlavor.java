package ui.table;

import cells.CellRange;

import java.awt.datatransfer.DataFlavor;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetDataFlavor extends DataFlavor {

    private static SpreadSheetDataFlavor instance;

    private SpreadSheetDataFlavor() {
        super(CellRange.class, "Range");
    }

    public static DataFlavor getInstance() {
        if (instance == null) {
            instance = new SpreadSheetDataFlavor();
        }

        return instance;
    }
}
