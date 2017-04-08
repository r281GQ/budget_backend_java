package budget.controller;

import budget.assembler.GroupingAssembler;
import budget.model.Grouping;
import budget.model.User;
import budget.resource.GroupingResource;
import budget.service.interfaces.GroupingService;
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
public class GroupingController {

    @Autowired
    private GroupingService groupingService;

    @Autowired
    private GroupingAssembler groupingAssembler;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/groupings/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("@securityHelper.isUserProvidedPrincipal(returnObject.user.identifier)")
    GroupingResource get(@PathVariable("id") @P("id") long id){
        return groupingAssembler.toResource(groupingService.getById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/groupings", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#grouping.user.identifier)")
    void create(@RequestBody @P("grouping") Grouping grouping){
        groupingService.create(grouping);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/groupings/{id}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesGroupingBelongToLoggedInUser(#id)")
    void delete(@PathVariable("id") @P("id") long id){
        groupingService.delete(wrapGrouping(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/groupings", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesGroupingBelongToLoggedInUser(#id)")
    void update(@RequestBody @P("grouping") Grouping grouping){
        groupingService.update(grouping);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/users/{id}/groupings", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#identifier)")
    List<GroupingResource> getGroupingsByUser(@PathVariable ("id") @P("id") long id){
        return groupingService.getByUser(wrapUserId(id)).stream().map(account -> groupingAssembler.toResource(account)).collect(Collectors.toList());
    }

    private User wrapUserId(long id){
        User user = new User();
        user.setIdentifier(id);
        return user;
    }

    private Grouping wrapGrouping(long id) {
        Grouping grouping = new Grouping();
        grouping.setIdentifier(id);
        return grouping;
    }
}
