package ui.preferences;

import ui.preferences.cellcount.CellCountPreferencesPanel;
import ui.preferences.color.ColorPreferencesPanel;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for customize table
 * @see {@link ColorPreferencesPanel}
 * @see {@link CellCountPreferencesPanel}
 * @author Dmitriy Tseyler
 */
public class PreferencesPanel extends JPanel{
    public PreferencesPanel(SpreadSheetTable table) {
        super(new BorderLayout());
        add(new ColorPreferencesPanel(table), BorderLayout.NORTH);
        add(new CellCountPreferencesPanel(table), BorderLayout.CENTER);
    }
}
