package budget.service.interfaces;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.Equity;
import budget.model.User;

import java.util.List;

/**
 * Created by veghe on 25/11/2016.
 */

public interface EquityService {
    Equity getById(long id) throws ResourceNotFoundException;

    void create(Equity equity) throws InvalidDataProvidedException;

    void delete(Equity equity) throws ResourceNotFoundException;

    void update(Equity equity) throws InvalidDataProvidedException;

    List<Equity> getByUser (User user);
}
