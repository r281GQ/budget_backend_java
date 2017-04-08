package budget.assembler;

import budget.controller.GroupingController;
import budget.controller.UserController;
import budget.model.Grouping;
import budget.resource.GroupingResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by veghe on 01/12/2016.
 */
@Component
public class GroupingAssembler extends ResourceAssemblerSupport<Grouping, GroupingResource> {


    public GroupingAssembler() {
        super(GroupingController.class, GroupingResource.class);
    }

    @Override
    public GroupingResource toResource(Grouping entity) {

        GroupingResource groupingResource = new GroupingResource();

        groupingResource.setUser(entity.getUser());
        groupingResource.setType(entity.getType());
        groupingResource.setIdentifier(entity.getIdentifier());
        groupingResource.setName(entity.getName());

        Link transactions = linkTo(GroupingController.class).slash("groupings").slash(entity.getIdentifier()).slash("transactions").withRel("transactions");

        Link user = linkTo(UserController.class).slash("users").slash(entity.getUser().getIdentifier()).withRel("user");

        Link self = linkTo(GroupingController.class).slash("groupings").slash(entity.getIdentifier()).withSelfRel();

        groupingResource.add(transactions, self, user);

        return groupingResource;
    }
}
