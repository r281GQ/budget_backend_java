package budget.repository.interfaces;

import budget.model.Account;
import budget.model.User;

import java.util.List;

/**
 * Created by veghe on 10/08/2016.
 */
public interface AccountRepository extends Repository<Long, Account> {

    List<Account> findByUser(User user);

}
