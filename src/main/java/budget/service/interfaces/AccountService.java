package budget.service.interfaces;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.Account;
import budget.model.User;

import java.util.List;

/**
 * Created by veghe on 13/08/2016.
 */
public interface AccountService {
    Account getById(long id) throws ResourceNotFoundException;

    List<Account> getByUser(User user);

    void create (Account account) throws InvalidDataProvidedException;

    void update (Account account) throws  InvalidDataProvidedException, ResourceNotFoundException;

    void delete( Account account) throws ResourceNotFoundException;
}
