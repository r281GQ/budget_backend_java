package budget.service.interfaces;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.Grouping;
import budget.model.User;

import java.util.List;

/**
 * Created by veghe on 01/12/2016.
 */
public interface GroupingService {
    Grouping getById(long id) throws ResourceNotFoundException;

    void create(Grouping grouping) throws InvalidDataProvidedException;

    void delete(Grouping grouping) throws ResourceNotFoundException;

    void update(Grouping grouping) throws InvalidDataProvidedException;

    List<Grouping> getByUser(User user);
}
