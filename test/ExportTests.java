import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.ExpenseTrackerController;
import controller.ExportConstants;
import model.ExpenseTrackerModel;
import model.Transaction;
import view.ExpenseTrackerView;

public class ExportTests {

    private ExpenseTrackerModel model;
    private TestView view;
    private ExpenseTrackerController controller;

    public static class TestView extends ExpenseTrackerView {
        private String lastMessage;
        @Override
        public void showExportResultMessage(String msg) {
            this.lastMessage = msg;
        }
        public String getLastMessage() {
            return lastMessage;
        }
    }

    @Before
    public void setup() {
        model = new ExpenseTrackerModel();
        view = new TestView();
        controller = new ExpenseTrackerController(model, view);
    }

    @After
    public void cleanupFiles() {
        try {
            Files.deleteIfExists(Paths.get("test-output.csv"));
        } catch (Exception ignored) {}
        try {
            Files.deleteIfExists(Paths.get("ioerror.csv"));
        } catch (Exception ignored) {}
    }

    @Test
    public void testIsValidCsvFilename_variousNames() {
        assertTrue(controller.isValidCsvFilename("out.csv"));
        assertTrue(controller.isValidCsvFilename("Report_01.csv"));
        assertTrue(controller.isValidCsvFilename("my file.csv"));

        assertEquals(false, controller.isValidCsvFilename(""));
        assertEquals(false, controller.isValidCsvFilename("noext"));
        assertEquals(false, controller.isValidCsvFilename("bad/name.csv"));
        assertEquals(false, controller.isValidCsvFilename("bad\\name.csv"));
        assertEquals(false, controller.isValidCsvFilename("../secret.csv"));
    }

    @Test
    public void testExport_validFilename_createsCsvWithCorrectHeaderAndRows() throws IOException {
        model.addTransaction(new Transaction(12.5, "food"));
        model.addTransaction(new Transaction(7.0, "travel"));

        controller.exportTransactionsToCsv("test-output.csv");

        File f = new File("test-output.csv");
        assertTrue(f.exists());
        List<String> lines = Files.readAllLines(f.toPath());
        assertTrue(lines.size() >= 3);
        assertEquals(ExportConstants.FILE_HEADERS, lines.get(0));
        // Amounts should be formatted to two decimals
        assertTrue(lines.get(1).contains(",12.50,"));
        assertTrue(lines.get(2).contains(",07.00,") || lines.get(2).contains(",7.00,"));
    }

    @Test
    public void testExport_invalidFilename_displaysInvalidMessage() {
        controller.exportTransactionsToCsv("");
        assertEquals(ExportConstants.FILE_NAME_INVALID, view.getLastMessage());

        controller.exportTransactionsToCsv("bad/name.csv");
        assertEquals(ExportConstants.FILE_NAME_INVALID, view.getLastMessage());

        controller.exportTransactionsToCsv("noext");
        assertEquals(ExportConstants.FILE_NAME_INVALID, view.getLastMessage());
    }

    @Test
    public void testExport_ioException_displaysError() throws IOException {
        // Create a directory named ioerror.csv so opening as a file throws IOException
        Files.createDirectories(Paths.get("ioerror.csv"));
        controller.exportTransactionsToCsv("ioerror.csv");
        String msg = view.getLastMessage();
        assertTrue(msg.startsWith(ExportConstants.ERROR_IN_EXPORT));
    }
}

