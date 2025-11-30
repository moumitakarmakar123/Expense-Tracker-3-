package controller;

/**
 * Constants for the CSV export feature to avoid magic strings.
 */
public final class ExportConstants {
    public static final String FILE_HEADERS = "Date,Amount,Category";
    public static final String SUCCESSFUL_EXPORT = "Export successful: ";
    public static final String ERROR_IN_EXPORT = "Export failed: ";
    public static final String FILE_NAME_INVALID = "Invalid file name. Please provide a .csv name with safe characters.";

    private ExportConstants() {}
}

