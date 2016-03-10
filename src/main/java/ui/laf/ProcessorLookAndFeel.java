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
}
