package budget.repository.interfaces;

import budget.model.User;
import org.springframework.security.access.prepost.PostAuthorize;

import java.util.List;

/**
 * Created by veghe on 14/08/2016.
 */
public interface UserRepository extends Repository<Long, User> {

    User getByEmail(String email);

}
