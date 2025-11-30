package model.Filter;

import java.util.ArrayList;
import java.util.List;

import model.Transaction;
import controller.InputValidation;

/**
 * The CategoryFilter class only shows the Transactions with categories equal to the specified category.
 *
 * NOTE) This is applying the Strategy design pattern. This is a concrete strategy class.
 */
public class CategoryFilter implements TransactionFilter {
    private String categoryFilter;

    public CategoryFilter(String categoryFilter) {
        // Since the CategoryFilter constructor is public, 
        // the input validation needs to be performed again.
        if(!InputValidation.isValidCategory(categoryFilter)){
            throw new IllegalArgumentException("Invalid category filter");
        }else{
            this.categoryFilter = categoryFilter;
        }
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
	// Perform input validation
        if (transactions == null) {
            throw new IllegalArgumentException("The transactions list must be non-null.");
	}
	
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getCategory().equalsIgnoreCase(categoryFilter)) {
                filteredTransactions.add(transaction);
            }
        }

        return filteredTransactions;
    }
}
