package budget.controller.exceptions;

import budget.model.BudgetModel;

/**
 * Created by veghe on 19/12/2016.
 */
public class ResourceAlreadyExists extends RuntimeException {
    public ResourceAlreadyExists(BudgetModel budgetModel) {
    }
    public ResourceAlreadyExists(String string) {
    }
}
