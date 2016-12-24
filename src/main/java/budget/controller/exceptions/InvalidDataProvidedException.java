package budget.controller.exceptions;

import budget.model.BudgetModel;

/**
 * Created by veghe on 17/11/2016.
 */
public class InvalidDataProvidedException extends RuntimeException {

    private final String details;

    private final BudgetModel budgetModel;

    public InvalidDataProvidedException(String details, BudgetModel budgetModel) {
        this.details = details;
        this.budgetModel = budgetModel;
    }

    public BudgetModel getBudgetModel() {
        return budgetModel;
    }

    public String getDetails() {
        return details;
    }
}
