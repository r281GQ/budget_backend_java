package budget.controller;

import budget.assembler.EquityAssembler;
import budget.model.Equity;
import budget.model.User;
import budget.resource.EquityResource;
import budget.service.interfaces.EquityService;
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
public class EquityController {

    @Autowired
    private EquityService equityService;

    @Autowired
    private EquityAssembler equityAssembler;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/equities/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("@securityHelper.isUserProvidedPrincipal(returnObject.user.identifier)")
    public EquityResource get(@PathVariable("id") @P("id") long id) {
        return equityAssembler.toResource(equityService.getById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/equities", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#equity.user.identifier)")
    public void create(@RequestBody @P("equity") Equity equity) {
        equityService.create(equity);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/equities/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesEquityBelongToLoggedInUser(#id)")
    public void delete(@PathVariable("id") @P("id") long id) {
        equityService.delete(wrapEquity(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/equities", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.doesEquityBelongToLoggedInUser(#equity.identifier)")
    public void update(@RequestBody @P("equity") Equity equity) {
        equityService.update(equity);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/users/{id}/equities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@securityHelper.isUserProvidedPrincipal(#id)")
    public List<EquityResource> getEquitiesByUser(@PathVariable ("id") @P("id") long id) {
        return equityService.getByUser(wrapUserId(id)).stream().map(equity -> equityAssembler.toResource(equity)).collect(Collectors.toList());
    }

    private User wrapUserId(long id) {
        User user = new User();
        user.setIdentifier(id);
        return user;
    }

    private Equity wrapEquity(long id) {
        Equity equity = new Equity();
        equity.setIdentifier(id);
        return equity;
    }
}
