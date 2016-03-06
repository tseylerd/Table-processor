package main.java.ui.icon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Dmitriy Tseyler
 */
public class IconFactory {
    private static final IconFactory INSTANCE = new IconFactory();
    private static final String ICON_PATH = "resources/%s";

    private final Map<IconConfig, Icon> icons;

    private IconFactory() {
        icons = new EnumMap<>(IconConfig.class);
    }

    public Icon getIcon(IconConfig config) {
        return null;
    }
}
