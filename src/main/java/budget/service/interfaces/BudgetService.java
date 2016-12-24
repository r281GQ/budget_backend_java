package budget.service.interfaces;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.Budget;
import budget.model.User;

import java.util.List;

/**
 * Created by veghe on 11/09/2016.
 */
public interface BudgetService {

    void delete(Budget budget) throws ResourceNotFoundException;

    Budget getById(long id) throws ResourceNotFoundException;

    void create(Budget budget) throws InvalidDataProvidedException;

    void update(Budget budget) throws InvalidDataProvidedException;

    void updateWithoutReCalculation(Budget budget) throws InvalidDataProvidedException;

    List <Budget> getByUser(User user);
}
