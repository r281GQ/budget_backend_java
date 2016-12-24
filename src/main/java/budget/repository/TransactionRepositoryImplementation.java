package budget.repository;

import budget.model.*;
import budget.repository.interfaces.TransactionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by veghe on 14/08/2016.
 */
@Repository
@Transactional
public class TransactionRepositoryImplementation implements TransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Transaction get(Long key) {
        return entityManager.find(Transaction.class, key);
    }

    @Override
    public Transaction update(Transaction transaction) {
        entityManager.merge(transaction);
        return transaction;
    }

    @Override
    public Transaction delete(Long key) {
        Transaction transaction = get(key);
        entityManager.remove(entityManager.contains(transaction) ? transaction : entityManager.merge(transaction));
        return transaction;
    }

    @Override
    public Transaction create(Transaction transaction) {
        entityManager.persist(transaction);
        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        Query query = entityManager.createQuery("from Transaction");
        return query.getResultList();
    }

    @Override
    public List<Transaction> findByUser(User user) {
        Query query = entityManager.createQuery("from Transaction where USER_IDENTIFIER = :id");
        query.setParameter("id", user.getIdentifier());
        return query.getResultList();
    }

    @Override
    public List<Transaction> findByAccount(Account account) {
        Query query = entityManager.createQuery("from Transaction where ACCOUNT_IDENTIFIER = :id");
        query.setParameter("id", account.getIdentifier());
        return query.getResultList();
    }

    @Override
    public List<Transaction> findByBudget(Budget budget) {
        Query query = entityManager.createQuery("from Transaction where BUDGET_IDENTIFIER = :id");
        query.setParameter("id", budget.getIdentifier());
        return query.getResultList();
    }

    @Override
    public List<Transaction> findBudgetPeriod(BudgetPeriod budgetPeriod) {
        Query query = entityManager.createQuery("from Transaction where BUDGETPERIOD_IDENTIFIER = :id");
        query.setParameter("id", budgetPeriod.getIdentifier());
        return query.getResultList();
    }

    @Override
    public List<Transaction> findByEquity(Equity equity) {
        Query query = entityManager.createQuery("from Transaction where EQUITY_IDENTIFIER = :id");
        query.setParameter("id", equity.getIdentifier());
        return query.getResultList();
    }

    @Override
    public List<Transaction> findByGrouping(Grouping grouping) {
        Query query = entityManager.createQuery("from Transaction where GROUPING_IDENTIFIER = :id");
        query.setParameter("id", grouping.getIdentifier());
        return query.getResultList();
    }
}
