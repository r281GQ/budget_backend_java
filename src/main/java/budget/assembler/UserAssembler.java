package budget.assembler;

import budget.controller.UserController;
import budget.model.User;
import budget.resource.UserResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by veghe on 09/08/2016.
 */
@Component
public class UserAssembler extends ResourceAssemblerSupport <User, UserResource>{

    public UserAssembler() {
        super(UserController.class, UserResource.class);
    }

    @Override
    public UserResource toResource(User user) {
        UserResource userResource = new UserResource();

        userResource.setName(user.getName());
        userResource.setPassword(user.getPassword());
        userResource.setIdentifier(user.getIdentifier());
        userResource.setEmail(user.getEmail());
        userResource.setRole(user.getRole());

        Link self = linkTo(UserController.class).slash("users").slash(user.getIdentifier()).withSelfRel();

        Link transactions = linkTo(UserController.class).slash("users").slash(user.getIdentifier()).slash("transactions").withRel("transactions");

        Link accounts = linkTo(UserController.class).slash("users").slash(user.getIdentifier()).slash("accounts").withRel("accounts");

        Link groupings = linkTo(UserController.class).slash("users").slash(user.getIdentifier()).slash("groupings").withRel("groupings");

        Link equities = linkTo(UserController.class).slash("users").slash(user.getIdentifier()).slash("equities").withRel("equities");

        Link budgets = linkTo(UserController.class).slash("users").slash(user.getIdentifier()).slash("budgets").withRel("budgets");

        Link budgetPeriods = linkTo(UserController.class).slash("users").slash(user.getIdentifier()).slash("budgetPeriods").withRel("budgetPeriods");

        userResource.add(transactions, accounts, groupings, equities, budgetPeriods, budgets, self);

        return userResource;
    }
}
