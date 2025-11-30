package model;

import controller.InputValidation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    
  // final means that the variable cannot be changed
  private final double amount;
  private final String category;
  private final String timestamp;

  public Transaction(double amount, String category) {
    // Since this is a public constructor, perform input validation
    // to guarantee that the amount and category are both valid
    if (!InputValidation.isValidAmount(amount)) {
      throw new IllegalArgumentException("The amount is not valid.");
    }
    if (!InputValidation.isValidCategory(category)) {
      throw new IllegalArgumentException("The category is not valid.");
    }
      
    this.amount = amount;
    this.category = category;
    this.timestamp = generateTimestamp();
  }

  public double getAmount() {
    return amount;
  }

  public String getCategory() {
    return category;
  }
  
  public String getTimestamp() {
    return timestamp;
  }

  // private helper method to generate timestamp
  private String generateTimestamp() {
     return LocalDateTime.now().format(DATE_FORMATTER);
  }

}
