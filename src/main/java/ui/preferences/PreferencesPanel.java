package ui.preferences;

import ui.preferences.grid.ColorPreferencesPanel;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class PreferencesPanel extends JPanel{
    public PreferencesPanel(SpreadSheetTable table) {
        super(new BorderLayout());
        add(new ColorPreferencesPanel(table), BorderLayout.NORTH);
    }
}
