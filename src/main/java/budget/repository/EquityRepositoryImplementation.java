package budget.repository;

import budget.model.Equity;
import budget.model.User;
import budget.repository.interfaces.EquityRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by veghe on 18/11/2016.
 */
@Repository
@Transactional
public class EquityRepositoryImplementation implements EquityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Equity get(Long key) {
        return entityManager.find(Equity.class, key);
    }

    @Override
    public Equity update(Equity equity) {
        entityManager.merge(equity);
        return get(equity.getIdentifier());
    }

    @Override
    public Equity delete(Long key) {
        Equity equity = get(key);
        entityManager.remove(entityManager.contains(equity) ? equity : entityManager.merge(equity));
        return get(key);
    }

    @Override
    public Equity create(Equity equity) {
        entityManager.persist(equity);
        return equity;
    }

    @Override
    public List<Equity> findAll() {
        Query query = entityManager.createQuery("from Equity");
        return query.getResultList();
    }

    @Override
    public List<Equity> findByUser(User user) {
        Query query = entityManager.createQuery("from Equity where USER_IDENTIFIER = :id");
        query.setParameter("id", user.getIdentifier());
        return query.getResultList();
    }
}
