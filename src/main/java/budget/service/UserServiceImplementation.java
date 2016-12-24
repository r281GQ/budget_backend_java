package budget.service;

import budget.accessories.validators.ValidationService;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceAlreadyExists;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.User;
import budget.repository.interfaces.UserRepository;
import budget.service.interfaces.DefaultValueProviderService;
import budget.service.interfaces.DeleteHelperService;
import budget.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by veghe on 16/11/2016.
 */
@Service
@Transactional
public class UserServiceImplementation implements UserService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private DefaultValueProviderService defaultValueProviderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeleteHelperService deleteHelperService;

    @Override
    public User getById(Long id) {
        User user = userRepository.get(id);
        if (user == null) {
            user = new User();
            user.setIdentifier(id);
            throw new ResourceNotFoundException(user);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = userRepository.getByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            throw new ResourceNotFoundException(user);
        }
        return user;
    }

    @Override
    public void delete(User user) {
        nullCheck(user);

        if (!hasIdentifier(user) || (!isUserInDB(user)))
            throw new ResourceNotFoundException(user);
        deleteHelperService.deleteUser(user);
    }

    @Override
    public void create(User user) {
        nullCheck(user);

        if (hasIdentifier(user))
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.ID_PRESENT_ON_CREATION), user);

        checkIfUserExistsBasedOnEmail(user);

        setDefaultRole(user);

        userRepository.create(user);
    }

    private void setDefaultRole(User user) {
        user.setRole("USER");
    }

    @Override
    public void update(User user) {
        nullCheck(user);

        if (!hasIdentifier(user) || !isUserInDB(user))
            throw new ResourceNotFoundException(user);
        if (!validationService.isUpdateAble(user))
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.INVALIDATION_USER), user);

        userRepository.update(user);
    }

    private boolean isUserInDB(User user) {
        return userRepository.get(user.getIdentifier()) != null;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    private void checkIfUserExistsBasedOnEmail(User user) {
        User inDB = null;
        try {
            inDB = getByEmail(user.getEmail());
            throw new ResourceAlreadyExists("User with email: \"" + user.getEmail() + "\" already exists in the database.");
        } catch (ResourceNotFoundException e) {
        }
    }

    private void nullCheck(User user) {
        if (user == null) {
            user = new User();
            user.setIdentifier(0L);
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.NO_CONTENT_PROVIDED),user);
        }
    }

    private boolean hasIdentifier(User user) {
        return user.getIdentifier() != null;
    }
}
