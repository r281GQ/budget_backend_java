package budget.controller;

import budget.assembler.UserAssembler;
import budget.model.User;
import budget.resource.UserResource;
import budget.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by veghe on 28/07/2016.
 */

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAssembler userAssembler;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#id)")
    public UserResource get(@PathVariable ("id") @P("id") long id){ return userAssembler.toResource(userService.getById(id));}

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/users", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#user.identifier)")
    public void update(@RequestBody @P("user") @Valid User user){userService.update(user);}

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#id)")
    public void delete(@PathVariable ("id") @P("id") long id){
        userService.delete(wrapUser(id));}

    private User wrapUser(long id) {
        User toDelete = new User();
        toDelete.setIdentifier(id);
        return toDelete;
    }
}
