package ui.preferences.color;

import cells.CellRange;
import ui.laf.ProcessorUIDefaults;
import ui.laf.grid.BorderMode;
import ui.table.SpreadSheetModel;
import ui.table.SpreadSheetTable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.List;

/**
 * @author Dmitriy Tseyler
 */
public class ColorPreferencesPanel extends JPanel {
    private final SpreadSheetTable table;
    private final Map<BorderMode, JCheckBox> checkBoxes;
    private final JButton gridColorButton;
    private final JButton backgroundColorButton;
    private final JButton resetButton;

    private Color gridColor = ProcessorUIDefaults.DEFAULT_GRID_COLOR;
    private Color backgroundColor = ProcessorUIDefaults.DEFAULT_GRID_COLOR;

    public ColorPreferencesPanel(SpreadSheetTable table) {
        super(new GridBagLayout());
        this.table = table;
        checkBoxes = new EnumMap<>(BorderMode.class);

        for (BorderMode borderMode : BorderMode.values()) {
            createBorderCheckBox(borderMode);
        }

        table.getSelectionModel().addListSelectionListener(this::selectionChanged);
        table.getColumnModel().addColumnModelListener(new TableColumnModelAdapter() {
            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
                selectionChanged(e);
            }
        });

        gridColorButton = new InverseColorButton(new AbstractAction("Grid color") {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseGridColor();
            }
        });
        backgroundColorButton = new InverseColorButton(new AbstractAction("Background color") {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseBackgroundColor();
            }
        });
        resetButton = new JButton(new AbstractAction("Restore defaults") {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        addComponents();

        setBorder(new TitledBorder("Color settings"));
        selectionChanged(null);
    }

    private void chooseColor(Supplier<Color> getter, Consumer<Color> setter, BiConsumer<CellRange, Color> modelSetter,
                             JButton holder)
    {
        Color newColor = getter.get();
        CellRange range = CellRange.createCellRange(table.getSelectedRows(), table.getSelectedColumns());
        newColor = JColorChooser.showDialog(JOptionPane.getRootFrame(), "Select color", newColor);
        if (newColor == null) {
            return;
        }
        setter.accept(newColor);
        modelSetter.accept(range, newColor);
        holder.setBackground(newColor);
        table.repaint(50);
    }

    private void chooseGridColor() {
        chooseColor(this::getGridColor, this::setGridColor, table.getTableColorModel()::setRangeLineColor, gridColorButton);
    }

    private void chooseBackgroundColor() {
        chooseColor(this::getBackgroundColor, this::setBackgroundColor, table.getTableColorModel()::setBackgroundColor, backgroundColorButton);
    }

    private Color getBackgroundColor() {
        return backgroundColor;
    }

    private Color getGridColor() {
        return gridColor;
    }

    private void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    private void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    private void selectionChanged(ListSelectionEvent e) {
        if (e != null && e.getValueIsAdjusting()) {
            return;
        }

        CellRange range = CellRange.createCellRange(table.getSelectedRows(), table.getSelectedColumns());

        List<BorderMode> modesList = table.getTableColorModel().getModes(range);
        for (Map.Entry<BorderMode, JCheckBox> borderModeCheckBoxEntry : checkBoxes.entrySet()) {
            JCheckBox checkBox = borderModeCheckBoxEntry.getValue();
            BorderMode mode = borderModeCheckBoxEntry.getKey();
            checkBox.setSelected(modesList != null && modesList.contains(mode));
            checkBox.setEnabled(modesList != null && mode.isModeEnabled((SpreadSheetModel)table.getModel(), range));
        }

        //gridColor = table.getTableColorModel().getGridColor(range);
       // gridColorButton.setBackground(gridColor);
        //backgroundColor = table.getTableColorModel().getBackgroundColor(range);
       // backgroundColorButton.setBackground(backgroundColor);
        boolean enabled = range != null;
        backgroundColorButton.setEnabled(enabled);
        gridColorButton.setEnabled(enabled);
        resetButton.setEnabled(enabled);
    }

    private void createBorderCheckBox(BorderMode mode) {
        BorderModeCheckBox checkBox = new BorderModeCheckBox(mode);
        checkBox.addActionListener(e -> {
            CellRange range = CellRange.createCellRange(table.getSelectedRows(), table.getSelectedColumns());
            BorderMode borderMode = checkBox.getMode();
            borderMode.setModePreferences(table.getTableColorModel(), range, checkBox.isSelected());
            List<BorderMode> modeList = new ArrayList<>();
            for (Map.Entry<BorderMode, JCheckBox> borderModeCheckBoxEntry : checkBoxes.entrySet()) {
                if (borderModeCheckBoxEntry.getValue().isSelected()) {
                    modeList.add(borderModeCheckBoxEntry.getKey());
                }
            }
            table.getTableColorModel().addBorderModes(range, modeList);
            table.repaint(50);
        });
        checkBoxes.put(mode, checkBox);
    }

    private void reset() {
        CellRange range = CellRange.createCellRange(table.getSelectedRows(), table.getSelectedColumns());
        if (range == null)
            return;

        table.getTableColorModel().reset(range);
        selectionChanged(null);
        table.repaint(50);
    }

    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        for (JCheckBox checkBox : checkBoxes.values()) {
            add(checkBox, gbc);
            gbc.gridy++;
        }

        add(gridColorButton, gbc);
        gbc.gridy++;
        add(backgroundColorButton, gbc);
        gbc.gridy++;
        add(resetButton, gbc);
        gbc.gridy++;

        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        add(Box.createGlue(), gbc);
    }
}
