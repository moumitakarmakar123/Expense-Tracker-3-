package controller;

import view.ExpenseTrackerView;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import model.ExpenseTrackerModel;
import model.Transaction;
import model.Filter.TransactionFilter;

public class ExpenseTrackerController {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  /** 
   * The Controller is applying the Strategy design pattern.
   * This is the has-a relationship with the Strategy class 
   * being used in the applyFilter method.
   */
  private TransactionFilter filter;

  public ExpenseTrackerController(ExpenseTrackerModel model, ExpenseTrackerView view) {
    this.model = model;
    this.view = view;
  }

  /**
   * Sets the Strategy class being used in the applyFilter method.
   *
   * @param filter The concrete strategy class to be used for filtering
   */
  public void setFilter(TransactionFilter filter) {
    this.filter = filter;
  }

  public void refresh() {
    List<Transaction> transactions = model.getTransactions();
    view.refreshTable(transactions);
  }

  public boolean addTransaction(double amount, String category) {
    if (!InputValidation.isValidAmount(amount)) {
      return false;
    }
    if (!InputValidation.isValidCategory(category)) {
      return false;
    }
    
    Transaction t = new Transaction(amount, category);
    model.addTransaction(t);
    view.getTableModel().addRow(new Object[]{t.getAmount(), t.getCategory(), t.getTimestamp()});
    refresh();
    return true;
  }

  /**
   * Adds a transaction but returns a user-friendly error message on failure.
   * Returns null when the add was successful.
   */
  public String addTransactionWithMessage(double amount, String category) {
    if (!InputValidation.isValidAmount(amount)) {
      return "Amount must be > 0 and <= 1000";
    }
    if (!InputValidation.isValidCategory(category)) {
      return "Category must be one of: food, travel, bills, entertainment, other";
    }

    try {
      Transaction t = new Transaction(amount, category);
      model.addTransaction(t);
      view.getTableModel().addRow(new Object[]{t.getAmount(), t.getCategory(), t.getTimestamp()});
      refresh();
      return null;
    } catch (IllegalArgumentException ex) {
      // Return the constructor's message if validation inside Transaction fails
      return ex.getMessage();
    }
  }

  /**
   * Applies the filter specified by the user.
   *
   * NOTE) This is applying the Strategy design pattern. This is the core method using the strategy helper method.
   */
  public void applyFilter() {
    List<Transaction> filteredTransactions;
    // If no filter is specified, show all transactions.
    if (filter == null) {
      filteredTransactions = model.getTransactions();
    }
    // If a filter is specified, show only the transactions accepted by that filter.
    else {
      // Use the Strategy class to perform the desired filtering
      List<Transaction> transactions = model.getTransactions();
      filteredTransactions = filter.filter(transactions);
    }
    view.displayFilteredTransactions(filteredTransactions);
  }
  
  /**
   * Validates that the provided name is a safe CSV filename.
   */
  public boolean isValidCsvFilename(String name) {
    if (name == null) {
      return false;
    }
    String trimmed = name.trim();
    if (trimmed.isEmpty()) {
      return false;
    }
    String lower = trimmed.toLowerCase();
    if (!lower.endsWith(".csv")) {
      return false;
    }
    if (trimmed.contains("/") || trimmed.contains("\\") || trimmed.contains("..")) {
      return false;
    }
    if (!trimmed.matches("[A-Za-z0-9 _\\-\\.]+")) {
      return false;
    }
    return true;
  }

  /**
   * Escapes a value for CSV: doubles quotes and wraps in quotes.
   */
  public String escapeCsv(String s) {
    if (s == null) {
      return "\"\"";
    }
    String replaced = s.replace("\"", "\"\"");
    return "\"" + replaced + "\"";
  }

  /**
   * Generates CSV content from the provided transactions list.
   */
  public String generateCsvContent(List<Transaction> transactions) {
    StringBuilder sb = new StringBuilder();
    sb.append(ExportConstants.FILE_HEADERS).append("\n");
    for (Transaction t : transactions) {
      String date = t.getTimestamp();
      String amountStr = String.format("%.2f", t.getAmount());
      String category = escapeCsv(t.getCategory());
      sb.append(date).append(',').append(amountStr).append(',').append(category).append("\n");
    }
    return sb.toString();
  }

  /**
   * Exports all transactions to a CSV file and reports the result via the View.
   */
  public void exportTransactionsToCsv(String filename) {
    if (!isValidCsvFilename(filename)) {
      view.showExportResultMessage(ExportConstants.FILE_NAME_INVALID);
      return;
    }
    List<Transaction> transactions = model.getTransactions();
    String content = generateCsvContent(transactions);
    try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) {
      writer.print(content);
      view.showExportResultMessage(ExportConstants.SUCCESSFUL_EXPORT + filename);
    } catch (IOException e) {
      view.showExportResultMessage(ExportConstants.ERROR_IN_EXPORT + e.getMessage());
    }
  }
    
}
