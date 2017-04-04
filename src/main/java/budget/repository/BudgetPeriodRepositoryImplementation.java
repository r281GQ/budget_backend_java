package budget.repository;

import budget.model.Budget;
import budget.model.BudgetPeriod;
import budget.model.Period;
import budget.model.User;
import budget.repository.interfaces.BudgetPeriodRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by veghe on 11/09/2016.
 */
@Repository
@Transactional
public class BudgetPeriodRepositoryImplementation implements BudgetPeriodRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BudgetPeriod get(Long key) {
        return entityManager.find(BudgetPeriod.class, key);
    }

    @Override
    public BudgetPeriod update(BudgetPeriod budgetPeriod) {
        entityManager.merge(budgetPeriod);
        return budgetPeriod;
    }

    @Override
    public BudgetPeriod delete(Long key) {
        BudgetPeriod budgetPeriod = get(key);
        entityManager.remove(entityManager.contains(budgetPeriod) ? budgetPeriod : entityManager.merge(budgetPeriod));
        return budgetPeriod;
    }

    @Override
    public BudgetPeriod create(BudgetPeriod budgetPeriod) {
        entityManager.persist(budgetPeriod);
        return budgetPeriod;
    }

    @Override
    public List<BudgetPeriod> findAll() {
        Query query = entityManager.createQuery("from BudgetPeriod");
        return query.getResultList();
    }

    @Override
    public List<BudgetPeriod> findByBudget(Budget budget) {
        Query q = entityManager.createQuery("from BudgetPeriod where BUDGET_IDENTIFIER = :id");
        q.setParameter("id", budget.getIdentifier());
        return q.getResultList();
    }

    @Override
    public List<BudgetPeriod> findByBudgetOrderedByDate(Budget budget) {
        Query q = entityManager.createQuery("from BudgetPeriod where BUDGET_IDENTIFIER = :id ORDER BY PERIOD ASC");
        q.setParameter("id", budget.getIdentifier());
        return q.getResultList();
    }

    @Override
    public BudgetPeriod findByBudgetAndPeriod(Budget budget, Period period) {
        Query q = entityManager.createQuery("from BudgetPeriod where BUDGET_IDENTIFIER = :id AND REPRESENTATION = :representation");
        q.setParameter("id", budget.getIdentifier());
        q.setParameter("representation", period.getRepresentation());
        List<BudgetPeriod> budgetPeriods = q.getResultList();
        budgetPeriods.forEach((g)-> System.out.print(g.getName()));
        return budgetPeriods.isEmpty() ? null : budgetPeriods.get(0);
    }

    @Override
    public List<BudgetPeriod> findByUser(User user) {
        Query q = entityManager.createQuery("from BudgetPeriod where USER_IDENTIFIER = :id");
        q.setParameter("id", user.getIdentifier());
        return q.getResultList();
    }
}
