package budget.assembler;

import budget.controller.BudgetController;
import budget.controller.BudgetPeriodController;
import budget.controller.UserController;
import budget.model.Budget;
import budget.model.BudgetPeriod;
import budget.resource.BudgetPeriodResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by veghe on 21/11/2016.
 */
@Component
public class BudgetPeriodAssembler extends ResourceAssemblerSupport<BudgetPeriod, BudgetPeriodResource> {

    public BudgetPeriodAssembler() {
        super(BudgetPeriodController.class, BudgetPeriodResource.class);
    }

    @Override
    public BudgetPeriodResource toResource(BudgetPeriod entity) {
        return assignValues(entity);
    }

    private BudgetPeriodResource assignValues(BudgetPeriod entity) {

        BudgetPeriodResource budgetPeriodResource = new BudgetPeriodResource();

        budgetPeriodResource.setIdentifier(entity.getIdentifier());
        budgetPeriodResource.setName(entity.getName());
        budgetPeriodResource.setAllowance(entity.getAllowance());
        budgetPeriodResource.setBalance(entity.getBalance());
        budgetPeriodResource.setPeriod(entity.getPeriod());
        budgetPeriodResource.setUser(entity.getUser());
        budgetPeriodResource.setBudget(prepareBudget(entity));

        Link self = linkTo(BudgetPeriodController.class).slash("budgetPeriods").slash(entity.getBudget().getIdentifier()).withSelfRel();
        Link user = linkTo(UserController.class).slash("users").slash(entity.getUser().getIdentifier()).withRel("user");
        Link transactions = linkTo(BudgetPeriodController.class).slash("budgetPeriods").slash(entity.getIdentifier()).slash("transactions").withRel("transactions");
        Link budget = linkTo(BudgetController.class).slash("budgets").slash(entity.getBudget().getIdentifier()).withRel("budget");

        budgetPeriodResource.add(self, user, budget, transactions);

        return budgetPeriodResource;
    }

    private Budget prepareBudget(BudgetPeriod entity) {
        Budget budget = entity.getBudget();
        budget.setUser(null);
        return budget;
    }
}
