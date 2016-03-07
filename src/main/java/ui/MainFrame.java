package ui;

import ui.table.SpreadSheetTable;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class MainFrame extends JFrame {
    private static final Dimension DEFAULT_SIZE = new Dimension(1024, 768);
    private static final String TITLE = "Tseyler table processor";

    public MainFrame() {
        setLayout(new BorderLayout());
        setTitle(TITLE);
        JScrollPane scrollPane = new JScrollPane(new SpreadSheetTable(),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(DEFAULT_SIZE);
        pack();
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}
