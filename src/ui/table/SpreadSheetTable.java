package ui.table;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetTable extends JTable {
    private static final int DEFAULT_ROW_COUNT = 40;
    private static final int DEFAULT_COLUMN_COUNT = 40;

    public SpreadSheetTable() {
        this(DEFAULT_ROW_COUNT, DEFAULT_COLUMN_COUNT);
        setCellSelectionEnabled(true);
        setDefaultEditor(CellValue.class, new SpreadSheetEditor());
        setDefaultRenderer(CellValue.class, new SpreadSheetRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setGridColor(Color.BLACK);
    }

    public SpreadSheetTable(int rowCount, int columnCount) {
        super(new SpreadSheetModel(rowCount, columnCount));
    }
}
