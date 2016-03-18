package ui.laf;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A small look and feel with one custom action and one custom UI.
 * @author Dmitriy Tseyler
 */
public class ProcessorLookAndFeel extends MetalLookAndFeel {

    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        Map<String, Object> defaults = new HashMap<>();
        defaults.put(ProcessorUIDefaults.SPREAD_SHEET_UI_ID, SpreadSheetTableUI.class.getName());
        table.putAll(defaults);
    }

    @Override
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);
        InputMap map = (InputMap) table.get("Table.ancestorInputMap");
        map.put(KeyStroke.getKeyStroke("BACK_SPACE"), ProcessorUIDefaults.TABLE_BACK_SPACE_ACTION);
    }
}
