package budget.controller;

import budget.assembler.AccountAssembler;
import budget.model.Account;
import budget.model.User;
import budget.resource.AccountResource;
import budget.service.interfaces.AccountService;
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
 * Created by veghe on 10/08/2016.
 */
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountAssembler accountAssembler;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("@securityHelper.isUserProvidedPrincipal(returnObject.user.identifier)")
    public AccountResource get(@PathVariable ("id") @P("id") long id){ return accountAssembler.toResource(accountService.getById(id));}

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/accounts", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#account.user.identifier)")
    public void create(@RequestBody @P("account") Account account){
        accountService.create(account);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/accounts" ,method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesAccountBelongToLoggedInUser(#account.identifier)")
    public void update(@RequestBody @P("account") Account account) {
        accountService.update(account);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesAccountBelongToLoggedInUser(#id)")
    public void delete(@PathVariable ("id") @P("id") long id){
       accountService.delete(wrapId(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/users/{id}/accounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#id)")
    public List<AccountResource> getAccountsByUser(@PathVariable ("id") @P("id") long id){
        return accountService.getByUser(wrapUserId(id)).stream().map(account -> accountAssembler.toResource(account)).collect(Collectors.toList());
    }

    private Account wrapId(long id){
        Account account = new Account();
        account.setIdentifier(id);
        return account;
    }

    private User wrapUserId(long id){
        User user = new User();
        user.setIdentifier(id);
        return user;
    }
}
