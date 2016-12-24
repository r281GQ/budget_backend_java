package budget.repository.interfaces;

import budget.model.Equity;
import budget.model.User;
import budget.repository.interfaces.Repository;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by veghe on 18/11/2016.
 */
public interface EquityRepository extends Repository<Long, Equity> {

    List<Equity> findByUser(User user);

}
