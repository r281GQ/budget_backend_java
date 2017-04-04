package budget.controller;

import budget.assembler.TransactionAssembler;
import budget.model.*;
import budget.resource.TransactionResource;
import budget.service.interfaces.TransactionService;
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
 * Created by veghe on 15/09/2016.
 */
@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionAssembler transactionAssembler;

//    @ResponseStatus(HttpStatus.OK)
//    @RequestMapping(value = "/transactions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PostAuthorize("returnObject.user.identifier == principal.user.identifier, @securityHelper.isUserProvidedPrincipal(returnObject.user.identifier)")
//    public TransactionResource get(@PathVariable("id") long id ){
//        return  transactionAssembler.toResource(transactionService.get(id));
//    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/transactions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("@securityHelper.isUserProvidedPrincipal(returnObject.user.identifier)")
    public TransactionResource get(@PathVariable("id") long id ){
        return  transactionAssembler.toResource(transactionService.get(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/transactions", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#transaction.user) and @securityHelper.doResourcesBelongToPrincipal(#transaction)")
    public void update(@RequestBody @P("transaction")  Transaction transaction)  {
        transactionService.update(transaction);
    }

//    @ResponseStatus(HttpStatus.OK)
//    @RequestMapping(value = "/transactions", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("#transaction.user.identifier == principal.user.identifier and @securityHelper.doResourcesBelongToPrincipal(#transaction) ")
//    public void update(@RequestBody @P("transaction")  Transaction transaction)  {
//        transactionService.update(transaction);
//    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/transactions" ,method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#transaction.user) and @securityHelper.doResourcesBelongToPrincipal(#transaction) ")
    public void create(@RequestBody  Transaction transaction)  {
        transactionService.create(transaction);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping( value = "/transactions/{id}",method = RequestMethod.DELETE)
    @PreAuthorize("@securityHelper.doesTransactionBelongToLoggedInUser(#id)")
    public void delete(@PathVariable ("id") long id)  {
        transactionService.delete(wrapId(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/users/{id}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#id)")
    public List<TransactionResource> getTransactionsByUser (@PathVariable ("id") @P("id") long id){
        return transactionService.getByUser(wrapIdForUser(id)).stream().map(account -> transactionAssembler.toResource(account)).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/accounts/{id}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesAccountBelongToLoggedInUser(#id)")
    public List<TransactionResource> getTransactionsByAccount (@PathVariable ("id") @P("id") long id){
        return transactionService.getByAccount(wrapIdForAccount(id)).stream().map(account -> transactionAssembler.toResource(account)).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/budgets/{id}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesBudgetBelongToLoggedInUser(#id)")
    public List<TransactionResource> getTransactionsByBudget (@PathVariable ("id") @P("id") long id){
        return transactionService.getByBudget(wrapIdForBudget(id)).stream().map(account -> transactionAssembler.toResource(account)).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/budgetPeriods/{id}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesBudgetPeriodBelongToLoggedInUser(#id)")
    public List<TransactionResource> getTransactionsByBudgetPeriod (@PathVariable ("id") @P("id") long id){
        return transactionService.getByBudgetPeriod(wrapIdForBudgetPeriod(id)).stream().map(account -> transactionAssembler.toResource(account)).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/equities/{id}/transactions",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesEquityBelongToLoggedInUser(#id)")
    public List<TransactionResource> getTransactionsByEquity (@PathVariable ("id") @P("id") long id){
        return transactionService.getByEquity(wrapIdForEquity(id)).stream().map(account -> transactionAssembler.toResource(account)).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/groupings/{id}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesGroupingBelongToLoggedInUser(#id)")
    public List<TransactionResource> getTransactionsByGrouping (@PathVariable ("id") @P("id") long id){
        return transactionService.getByGrouping(wrapIdForGrouping(id)).stream().map(account -> transactionAssembler.toResource(account)).collect(Collectors.toList());
    }

    private Transaction wrapId(long id) {
        Transaction transaction = new Transaction();
        transaction.setIdentifier(id);
        return transaction;
    }

    private Account wrapIdForAccount(long id) {
        Account account = new Account();
        account.setIdentifier(id);
        return account;
    }

    private User wrapIdForUser(long id) {
        User user = new User();
        user.setIdentifier(id);
        return user;
    }

    private Budget wrapIdForBudget(long id) {
        Budget budget = new Budget();
        budget.setIdentifier(id);
        return budget;
    }

    private BudgetPeriod wrapIdForBudgetPeriod(long id) {
        BudgetPeriod budgetPeriod = new BudgetPeriod();
        budgetPeriod.setIdentifier(id);
        return budgetPeriod;
    }

    private Grouping wrapIdForGrouping(long id) {
        Grouping grouping = new Grouping();
        grouping.setIdentifier(id);
        return grouping;
    }

    private Equity wrapIdForEquity(long id) {
        Equity equity = new Equity();
        equity.setIdentifier(id);
        return equity;
    }
}
