package budget.assembler;

import budget.controller.BudgetController;
import budget.controller.BudgetPeriodController;
import budget.controller.TransactionController;
import budget.controller.UserController;
import budget.model.Budget;
import budget.resource.BudgetResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by veghe on 20/11/2016.
 */
@Component
public class BudgetAssembler extends ResourceAssemblerSupport <Budget, BudgetResource> {

    public BudgetAssembler (){
        super(BudgetController.class, BudgetResource.class);
    }

    @Override
    public BudgetResource toResource(Budget entity) {
        BudgetResource budgetResource = new BudgetResource();

        budgetResource.setIdentifier(entity.getIdentifier());
        budgetResource.setDefaultAllowance(entity.getDefaultAllowance());
        budgetResource.setUser(entity.getUser());
        budgetResource.setName(entity.getName());

        Link user = linkTo(UserController.class).slash("users").slash(entity.getUser().getIdentifier()).withRel("user");
        Link self = linkTo(BudgetController.class).slash("budgets").slash(entity.getIdentifier()).withSelfRel();
        Link budgetPeriods = linkTo(BudgetPeriodController.class).slash("budgets").slash(entity.getIdentifier()).slash("budgetPeriods").withRel("budgetPeriods");
        Link transactions = linkTo(TransactionController.class).slash("budgets").slash(entity.getIdentifier()).slash("transactions").withRel("transactions");

        budgetResource.add(self, user, transactions, budgetPeriods);

        return budgetResource;
    }
}
