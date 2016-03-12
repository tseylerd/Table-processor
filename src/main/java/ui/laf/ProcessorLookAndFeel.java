package ui.laf;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * @author Dmitriy Tseyler
 */
public class ProcessorLookAndFeel extends MetalLookAndFeel {

    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        table.put("SpreadSheetTableUI", SpreadSheetTableUI.class.getName());
    }

    @Override
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);
        InputMap map = (InputMap) table.get("Table.ancestorInputMap");
        map.put(KeyStroke.getKeyStroke("BACK_SPACE"), ProcessorUIDefaults.TABLE_BACK_SPACE_ACTION);
    }
}
