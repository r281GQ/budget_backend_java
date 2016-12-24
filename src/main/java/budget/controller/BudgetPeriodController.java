package budget.controller;

import budget.assembler.BudgetPeriodAssembler;
import budget.model.Budget;
import budget.model.BudgetPeriod;
import budget.model.User;
import budget.resource.BudgetPeriodResource;
import budget.service.interfaces.BudgetPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by veghe on 17/11/2016.
 */
@RestController
public class BudgetPeriodController {

    @Autowired
    private BudgetPeriodService budgetPeriodService;

    @Autowired
    private BudgetPeriodAssembler budgetPeriodAssembler;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/budgetPeriods/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("returnObject.body.user.identifier == principal.user.identifier")
    BudgetPeriodResource get(@PathVariable ("id") @P("id") long id){
        return budgetPeriodAssembler.toResource(budgetPeriodService.getById(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/budgetPeriods",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#budgetPeriod.user.identifier == principal.user.identifier and @securityHelper.isRealResource(#budgetPeriod) and @securityHelper.doesBudgetBelongToLoggedInUser(#budgetPeriod.budget.identifier)")
    void update(@RequestBody @P("budgetPeriod") BudgetPeriod budgetPeriod){
        budgetPeriodService.update(budgetPeriod);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/users/{id}/budgetPeriods", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#id == principal.user.identifier")
    List<BudgetPeriodResource> budgetPeriodsByUser(@PathVariable ("id") @P("id") long id){
        return budgetPeriodService.getByUser(wrapUserId(id)).stream().map(budgetPeriod -> budgetPeriodAssembler.toResource(budgetPeriod)).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/budgets/{id}/budgetPeriods", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesBudgetBelongToLoggedInUser(#id)")
    List<BudgetPeriodResource> budgetPeriodsByBudget(@PathVariable ("id") @P("id") long id){
        return budgetPeriodService.getByBudget(wrapBudgetId(id)).stream().map(budgetPeriod -> budgetPeriodAssembler.toResource(budgetPeriod)).collect(Collectors.toList());
    }

    private Budget wrapBudgetId(long id) {
        Budget budget = new Budget();
        budget.setIdentifier(id);
        return budget;
    }

    private BudgetPeriod wrapId(Long id){
        BudgetPeriod budgetPeriod = new BudgetPeriod();
        budgetPeriod.setIdentifier(id);
        return budgetPeriod;
    }

    private User wrapUserId(long id){
        User user = new User();
        user.setIdentifier(id);
        return user;
    }
}
