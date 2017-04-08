package budget.assembler;

import budget.controller.EquityController;
import budget.controller.UserController;
import budget.model.Equity;
import budget.resource.EquityResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by veghe on 25/11/2016.
 */
@Component
public class EquityAssembler extends ResourceAssemblerSupport<Equity, EquityResource> {

    public EquityAssembler() {
        super(EquityController.class, EquityResource.class);
    }

    @Override
    public EquityResource toResource(Equity entity) {
        EquityResource equityResource = new EquityResource();
        equityResource.setName(entity.getName());
        equityResource.setIdentifier(entity.getIdentifier());
        equityResource.setUser(entity.getUser());
        equityResource.setBalance(entity.getBalance());
        equityResource.setCurrency(entity.getCurrency());
        equityResource.setType(entity.getType());

        Link transactions = linkTo(EquityController.class).slash("equities").slash(entity.getIdentifier()).slash("transactions").withRel("transactions");

        Link user = linkTo(UserController.class).slash("users").slash(entity.getUser().getIdentifier()).withRel("user");

        Link self = linkTo(EquityController.class).slash("equities").slash(entity.getIdentifier()).withSelfRel();

        equityResource.add(transactions, user, self);

        return equityResource;
    }
}
