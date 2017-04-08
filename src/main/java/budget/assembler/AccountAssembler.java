package budget.assembler;

import budget.controller.AccountController;
import budget.controller.UserController;
import budget.model.Account;
import org.springframework.hateoas.Link;
import budget.resource.AccountResource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by veghe on 11/08/2016.
 */

@Component
public class AccountAssembler extends ResourceAssemblerSupport<Account, AccountResource> implements Serializable {

    public AccountAssembler() {
        super(AccountController.class, AccountResource.class);
    }

    @Override
    public AccountResource toResource(Account entity) {
        AccountResource accountResource = new AccountResource();

        Link user = ControllerLinkBuilder.linkTo(UserController.class).slash("users").slash(entity.getUser().getIdentifier()).withRel("user");
        Link self = ControllerLinkBuilder.linkTo(AccountController.class).slash("accounts").slash(entity.getIdentifier()).withSelfRel();
        Link transactions = ControllerLinkBuilder.linkTo(AccountController.class).slash("accounts").slash(entity.getIdentifier()).slash("transactions").withRel("transactions");

        accountResource.add(self, user, transactions);

        accountResource.setIdentifier(entity.getIdentifier());
        accountResource.setName(entity.getName());
        accountResource.setUser(entity.getUser());
        accountResource.setBalance(entity.getBalance());
        accountResource.setCurrency(entity.getCurrency());

        return accountResource;
    }
}
