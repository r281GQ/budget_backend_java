package budget.repository.interfaces;

import budget.model.Grouping;
import budget.model.User;

import java.util.List;

/**
 * Created by veghe on 27/09/2016.
 */
public interface GroupingRepository extends Repository<Long, Grouping> {
    List<Grouping> findByUser(User user);
}
