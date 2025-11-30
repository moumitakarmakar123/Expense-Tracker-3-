# Expense Tracker (CS520 HW2)

This project is the solution for **CS520 Homework 2: Design Patterns & Testing**.
It extends the original Expense Tracker app to improve modularity, testability, and usability using the MVC architecture and the Strategy design pattern.


## Overview

The Expense Tracker allows users to add and manage daily transactions while maintaining a running total.
This version adds filtering functionality (by amount or category), reuses input validation logic from Homework 1, and includes a design plan for exporting transactions to a CSV file.



## How to Compile and Run

From the terminal, navigate to the `src` directory and run:

```bash
cd src
javac ExpenseTrackerApp.java
java ExpenseTrackerApp
```

If compiled successfully, the Expense Tracker GUI will appear.


## Java Version

This project was compiled and tested with:

```
java version "1.8.0_471"
Java(TM) SE Runtime Environment (build 1.8.0_471-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.471-b09, mixed mode)
```

Please ensure your JDK version is compatible (JDK 8 recommended).

## Features

* **Add Transaction:**
  Enter a valid amount and category, then click **Add Transaction**.
  The transaction appears in the list, and the total cost updates automatically.

* **Filter Transactions (Strategy Pattern):**
  Filter by amount or category.
  Implemented using the `TransactionFilter` interface and concrete classes `AmountFilter` and `CategoryFilter` for reusability and extensibility.

* **Input Validation:**
  Reuses Homework 1 validation logic for both adding transactions and filtering.

## Testing

The test suite includes:

* The two original tests (from Homework 1)
* Five new test cases:

  * Add valid transaction
  * Invalid amount input
  * Invalid category input
  * Filter by amount
  * Filter by category

All tests pass successfully (see `test_screenshot.png`).


### **Usability: Export to CSV file**
For this feature, I would add an “Export to CSV” option in the UI where the user can enter a file name and click export. The controller will take that input, validate the file name (for example, must end with .csv and contain no invalid characters), and then get all transactions from the model. It will call a helper class like CSVExporter that writes the data to a CSV file with proper headers and one transaction per line. The user will get clear feedback on success or failure through the view. This follows MVC separation, avoids hardcoded strings, and keeps the controller open for adding new export formats later (like JSON) without changing existing code.


### **Project Structure**

```
expense_tracker/
│
├── src/              # Source files (MVC components)
├── test/             # JUnit test cases
├── lib/              # JUnit library
├── jdoc/             # Generated Javadoc
├── build.xml         # Ant build file
├── README.md         # Updated README
├── gitlog.txt        # Git commit log
├── test_screenshot.png
└── export.pdf        # Usability design plan

## Export to CSV

The application provides an "Export CSV" button. When clicked, you are prompted for a filename (e.g., `report.csv`). The controller validates the filename and writes a CSV with header `Date,Amount,Category`, one transaction per line, amount formatted to two decimals, and category values CSV-escaped.

Filename rules:
- Must end with `.csv`
- Allowed characters: letters, digits, space, `_ - .`
- Disallowed: `/`, `\`, and `..`

### Ant Tasks

Run with Ant:

```
ant clean compile
ant test
ant document
```
