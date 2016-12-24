package budget.repository;

import budget.model.User;
import budget.repository.interfaces.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by veghe on 14/08/2016.
 */
@Repository
public class UserRepositoryImplementation implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User get(Long aLong) {
        return entityManager.find(User.class, aLong);
    }

    @Override
    public User update(User user) {
        return entityManager.merge(user);
    }

    @Override
    public User delete(Long key) {
        User user = get(key);
        entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
        return user;
    }

    @Override
    public User create(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        Query query = entityManager.createQuery("from User");
        return query.getResultList();
    }

    @Override
    public User getByEmail(String email) {
        Query query = entityManager.createQuery("from User where email = :email");
        query.setParameter("email", email);

        try {
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
