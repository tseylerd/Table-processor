package ui.menu;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Dmitriy Tseyler
 */
public class TableFileChooser extends JFileChooser {
    public static final String FILE_FORMAT = ".tab";

    public TableFileChooser() {
        setAcceptAllFileFilterUsed(false);
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(FILE_FORMAT);
            }

            @Override
            public String getDescription() {
                return String.format("Table files (%s)", FILE_FORMAT);
            }
        };
        addChoosableFileFilter(fileFilter);
    }
}
