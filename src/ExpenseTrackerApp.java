import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import view.ExpenseTrackerView;
import model.Filter.AmountFilter;
import model.Filter.CategoryFilter;

public class ExpenseTrackerApp {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // Ensure GUI creation and interaction happen on the Event Dispatch Thread
    SwingUtilities.invokeLater(() -> {
      // Create MVC components
      ExpenseTrackerModel model = new ExpenseTrackerModel();
      ExpenseTrackerView view = new ExpenseTrackerView();
      ExpenseTrackerController controller = new ExpenseTrackerController(model, view);

      // Initialize view (constructor may already set visible)
      view.setVisible(true);

      // Handle add transaction button clicks with descriptive feedback
      view.getAddTransactionBtn().addActionListener(e -> {
        try {
          double amount = view.getAmountField();
          String category = view.getCategoryField();

          if (Double.isNaN(amount)) {
            JOptionPane.showMessageDialog(view, "Please enter a valid numeric amount (e.g. 200 or 200.00)", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
            view.toFront();
            return;
          }

          String error = controller.addTransactionWithMessage(amount, category);
          if (error != null) {
            JOptionPane.showMessageDialog(view, error, "Invalid Input", JOptionPane.ERROR_MESSAGE);
            view.toFront();
          }
        } catch (Exception ex) {
          // Catch any unexpected runtime errors and show a helpful dialog instead of crashing
          String msg = ex.getMessage() == null ? ex.toString() : ex.getMessage();
          JOptionPane.showMessageDialog(view, "An unexpected error occurred: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }
      });

      // Add action listener to the "Apply Category Filter" button
      view.addApplyCategoryFilterListener(e -> {
        try {
          String categoryFilterInput = view.getCategoryFilterInput();
          CategoryFilter categoryFilter = new CategoryFilter(categoryFilterInput);
          if (categoryFilterInput != null) {
            controller.setFilter(categoryFilter);
            controller.applyFilter();
          }
        } catch (IllegalArgumentException exception) {
          JOptionPane.showMessageDialog(view, exception.getMessage());
          view.toFront();
        }
      });

      // Add action listener to the "Apply Amount Filter" button
      view.addApplyAmountFilterListener(e -> {
        try {
          double amountFilterInput = view.getAmountFilterInput();
          AmountFilter amountFilter = new AmountFilter(amountFilterInput);
          if (Math.abs(amountFilterInput) > 0.0) {
            controller.setFilter(amountFilter);
            controller.applyFilter();
          }
        } catch (IllegalArgumentException exception) {
          JOptionPane.showMessageDialog(view, exception.getMessage());
          view.toFront();
        }
      });

      // Add action listener to the "Clear Filter" button
      view.addClearFilterListener(e -> {
        controller.setFilter(null);
        controller.applyFilter();
      });

      // Add action listener to the "Export CSV" button
      view.addExportListener(e -> {
        String filename = view.promptForExportFilename();
        if (filename != null) {
          controller.exportTransactionsToCsv(filename);
        }
      });
    });
  }
}
