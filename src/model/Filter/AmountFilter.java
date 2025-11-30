package model.Filter;

import java.util.ArrayList;
import java.util.List;

import model.Transaction;
import controller.InputValidation;

/**
 * The AmountFilter class only shows the Transactions with amounts approximately equal to the specified amount.
 *
 * NOTE) The Strategy design pattern is being applied. This is a concrete strategy class.
 */
public class AmountFilter implements TransactionFilter{
    private double amountFilter;
    private static final double EPSILON = 1e-6;

    public AmountFilter(double amountFilter){
        // Since the AmountFilter constructor is public, 
        // the input validation needs to be performed again.
        if(!InputValidation.isValidAmount(amountFilter)){
            throw new IllegalArgumentException("Invalid amount filter");
        } else {
            this.amountFilter = amountFilter;
        }
    }
    @Override
    public List<Transaction> filter(List<Transaction> transactions){
	// Perform input validation
	if (transactions == null) {
	    throw new IllegalArgumentException("The transactions list must be non-null.");
	}
        List<Transaction> filteredTransactions = new ArrayList<>();
        for(Transaction transaction : transactions){
            // Your solution could use a different comparison here.
            if (Math.abs(transaction.getAmount() - amountFilter) < EPSILON) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }
    
}
