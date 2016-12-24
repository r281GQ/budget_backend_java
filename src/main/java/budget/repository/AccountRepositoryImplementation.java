package budget.repository;

import budget.model.Account;
import budget.model.User;
import budget.repository.interfaces.AccountRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by veghe on 10/08/2016.
 */
@Repository
@Transactional
public class AccountRepositoryImplementation implements AccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Account create(Account account) {
        entityManager.persist(account);
        return account;
    }

    @Override
    public Account get(Long key) {
        return entityManager.find(Account.class, key);
    }

    @Override
    public Account update(Account account) {
        entityManager.merge(account);
        return account;
    }

    @Override
    public Account delete(Long key) {
        Account account = get(key);
        entityManager.remove(entityManager.contains(account) ? account : entityManager.merge(account));
        return account;
    }

    @Override
    public List<Account> findAll() {
        Query query = entityManager.createQuery("from Account");
        return query.getResultList();
    }

    @Override
    public List<Account> findByUser(User user) {

        Query query = entityManager.createQuery("from Account where USER_IDENTIFIER = :id");

        query.setParameter("id", user.getIdentifier());

        return query.getResultList();
    }
}
