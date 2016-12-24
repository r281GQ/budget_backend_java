package budget.service.interfaces;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.User;

import java.util.List;

/**
 * Created by veghe on 16/11/2016.
 */
public interface UserService {

    User getById (Long id) throws ResourceNotFoundException;

    User getByEmail(String email) throws ResourceNotFoundException;

    void delete (User user) throws ResourceNotFoundException;

    void create (User user) throws InvalidDataProvidedException;

    void update (User user) throws InvalidDataProvidedException;

    List<User> getAll();
}
