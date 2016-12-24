package budget.repository;

import budget.model.Budget;
import budget.model.User;
import budget.repository.interfaces.BudgetRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by veghe on 04/10/2016.
 */

@Repository
@Transactional
public class BudgetRepositoryImplementation implements BudgetRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Budget get(Long key) {
        return entityManager.find(Budget.class, key);
    }

    @Override
    public Budget update(Budget budget) {
        return entityManager.merge(budget);
    }

    @Override
    public Budget delete(Long key) {
        Budget budget = get(key);
        entityManager.remove(entityManager.contains(budget) ? budget : entityManager.merge(budget));
        return budget;
    }

    @Override
    public Budget create(Budget budget) {
        entityManager.persist(budget);
        return budget;
    }

    @Override
    public List<Budget> findAll() {
        Query query = entityManager.createQuery("from Budget");
        return query.getResultList();
    }

    @Override
    public List<Budget> findByUser(User user) {
        Query query = entityManager.createQuery("from Budget where USER_IDENTIFIER = :id");
        query.setParameter("id", user.getIdentifier());
        return query.getResultList();
    }
}
