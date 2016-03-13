package ui.icon;

/**
 * @author Dmitriy Tseyler
 */
public enum IconConfiguration {
    WARNING("warning"),
    CYCLIC_REFERENCE("cyclic_reference"),
    CLOSE("close");

    private final String name;

    IconConfiguration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
