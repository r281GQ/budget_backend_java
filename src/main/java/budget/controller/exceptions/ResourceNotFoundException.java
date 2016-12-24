package budget.controller.exceptions;

import budget.model.BudgetModel;

/**
 * Created by veghe on 11/08/2016.
 */
public class ResourceNotFoundException extends RuntimeException {

    private final BudgetModel resource;

    public ResourceNotFoundException(BudgetModel budgetModel) {
        this.resource = budgetModel;
    }

    public BudgetModel getResource() {
        return resource;
    }
}
