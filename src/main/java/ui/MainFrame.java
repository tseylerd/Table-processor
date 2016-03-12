package ui;

import ui.laf.ProcessorLookAndFeel;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * @author Dmitriy Tseyler
 */
public class MainFrame extends JFrame {
    private static final Dimension DEFAULT_SIZE = new Dimension(1024, 768);
    private static final String TITLE = "Tseyler table processor";

    public MainFrame() {
        setLayout(new BorderLayout());
        setTitle(TITLE);
        add(new SpreadSheetPanel(), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(DEFAULT_SIZE);
        pack();
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new ProcessorLookAndFeel());
        MainFrame frame = new MainFrame();
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            JOptionPane.showMessageDialog(frame, e);
            e.printStackTrace();
        });
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}
