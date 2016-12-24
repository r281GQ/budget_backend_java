package budget.controller;

import budget.assembler.BudgetAssembler;
import budget.model.Budget;
import budget.model.User;
import budget.resource.BudgetResource;
import budget.service.interfaces.BudgetService;
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
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private BudgetAssembler budgetAssembler;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/budgets/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("returnObject.body.user.identifier == principal.user.identifier")
    BudgetResource get(@PathVariable ("id") long id){
        return budgetAssembler.toResource(budgetService.getById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/budgets",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#budget.user.identifier == principal.user.identifier")
    void create(@RequestBody @P("budget") Budget budget){
        budgetService.create(budget);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/budgets/{id}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesBudgetBelongToLoggedInUser(#id)")
    void delete(@PathVariable("id") @P("id") long id){
        budgetService.delete(wrapBudget(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/budgets",method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#budget.user.identifier == principal.user.identifier && @securityHelper.isRealResource(#budget)")
    void update(@RequestBody @P("budget") Budget budget){
        budgetService.update(budget);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/users/{id}/budgets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#id == principal.user.identifier")
    List <BudgetResource> getBudgetsByUser(@PathVariable ("id") @P("id") long id){
        return budgetService.getByUser(wrapUserId(id)).stream().map(budget -> budgetAssembler.toResource(budget)).collect(Collectors.toList());
    }

    private User wrapUserId(long id) {
        User user = new User();
        user.setIdentifier(id);
        return null;
    }

    private Budget wrapBudget(long id) {
        Budget budget = new Budget();
        budget.setIdentifier(id);
        return budget;
    }
}
