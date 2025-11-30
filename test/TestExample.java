import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

import org.junit.Before;
import org.junit.Test;

import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import model.Transaction;
import model.Filter.AmountFilter;
import model.Filter.CategoryFilter;
import view.ExpenseTrackerView;

public class TestExample {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  private ExpenseTrackerController controller;

  public static final String CATEGORY_FOOD = "food";
  public static final String CATEGORY_ENTERTAINMENT = "entertainment";  
    
  @Before
  public void setup() {
    model = new ExpenseTrackerModel();
    view = new ExpenseTrackerView();
    controller = new ExpenseTrackerController(model, view);
  }

  public double getTotalCost() {
    double totalCost = 0.0;
    List<Transaction> allTransactions = model.getTransactions();
    for (Transaction transaction : allTransactions) {
      totalCost += transaction.getAmount();
    }
    return totalCost;
  }

  public void checkTransaction(double amount, String category, Transaction transaction) {
    assertEquals(amount, transaction.getAmount(), 0.01);
    assertEquals(category, transaction.getCategory());
    String transactionDateString = transaction.getTimestamp();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    LocalDateTime transactionDate = LocalDateTime.parse(transactionDateString, formatter);
    LocalDateTime nowDate = LocalDateTime.now();
    assertNotNull(transactionDate);
    assertNotNull(nowDate);
    // Allow ≤ 60 seconds difference
    assertTrue(Duration.between(transactionDate, nowDate).getSeconds() < 60);
  }

  /**
   * Test Case 1: Add Valid Transaction
   * Purpose: Verify that a valid transaction is added successfully.
   * Steps: Add a transaction (amount=50.00, category="food").
   * Expected Output: Transaction added, total cost updated correctly.
   */
  @Test
  public void testAddTransaction() {
    assertEquals(0, model.getTransactions().size());
    double amount = 50.0;
    String category = CATEGORY_FOOD;
    assertTrue(controller.addTransaction(amount, category));

    assertEquals(1, model.getTransactions().size());
    Transaction firstTransaction = model.getTransactions().get(0);
    checkTransaction(amount, category, firstTransaction);
    assertEquals(amount, getTotalCost(), 0.01);
  }


  /**
   * Test Case 2: Input Validation for Amount
   * Purpose: Verify invalid amount input is rejected.
   * Steps: Attempt to add a transaction with amount=0.00 and valid category.
   * Expected Output: Transaction not added, total cost unchanged.
   */
  @Test
  public void testInvalidAmountInput() {
    assertEquals(0, model.getTransactions().size());
    assertEquals(0.00, getTotalCost(), 0.01);

    boolean didAddTransaction = controller.addTransaction(0.00, CATEGORY_FOOD);
    assertFalse(didAddTransaction);
    assertEquals(0, model.getTransactions().size());
    assertEquals(0.00, getTotalCost(), 0.01);
  }

  /**
   * Test Case 3: Input Validation for Category
   * Purpose: Verify invalid category input is rejected.
   * Steps: Attempt to add a transaction with valid amount but empty category.
   * Expected Output: Transaction not added, total cost unchanged.
   */
  @Test
  public void testInvalidCategoryInput() {
    assertEquals(0, model.getTransactions().size());
    assertEquals(0.00, getTotalCost(), 0.01);

    boolean didAddTransaction = controller.addTransaction(50.00, "");
    assertFalse(didAddTransaction);
    assertEquals(0, model.getTransactions().size());
    assertEquals(0.00, getTotalCost(), 0.01);
  }

  /**
   * Test Case 4: Filter by Amount
   * Purpose: Verify filtering transactions by specific amount.
   * Steps: Add multiple transactions, apply filter for amount=50.0.
   * Expected Output: Only transactions with amount 50.0 are displayed.
   */
  @Test
  public void testFilterByAmount() {
    double[] amountsList = { 50.0, 30.0, 40.0 };
    String[] categoriesList = { CATEGORY_FOOD, CATEGORY_ENTERTAINMENT, CATEGORY_FOOD };
    for (int i = 0; i < amountsList.length; i++) {
      controller.addTransaction(amountsList[i], categoriesList[i]);
    }

    assertEquals(3, model.getTransactions().size());

    controller.setFilter(new AmountFilter(50.0));
    controller.applyFilter();

    List<Transaction> displayedTransactions = view.getDisplayedTransactions();
    assertEquals(1, displayedTransactions.size());
    assertEquals(50.0, displayedTransactions.get(0).getAmount(), 0.01);
  }

  /**
   * Test Case 5: Filter by Category
   * Purpose: Verify filtering transactions by category.
   * Steps: Add multiple transactions, apply filter for category="food".
   * Expected Output: Only transactions with category "food" are displayed.
   */
  @Test
  public void testFilterByCategory() {
    double[] amountsList = { 50.0, 30.0, 40.0 };
    String[] categoriesList = { CATEGORY_FOOD, CATEGORY_ENTERTAINMENT, CATEGORY_FOOD };
    for (int i = 0; i < amountsList.length; i++) {
      controller.addTransaction(amountsList[i], categoriesList[i]);
    }

    assertEquals(3, model.getTransactions().size());

    controller.setFilter(new CategoryFilter(CATEGORY_FOOD));
    controller.applyFilter();

    List<Transaction> displayedTransactions = view.getDisplayedTransactions();
    assertEquals(2, displayedTransactions.size());
    for (Transaction currDisplayedTransaction : displayedTransactions) {
      assertEquals(CATEGORY_FOOD, currDisplayedTransaction.getCategory());
    }
  }
}
