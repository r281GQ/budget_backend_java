package budget.repository;

import budget.model.Grouping;
import budget.model.User;
import budget.repository.interfaces.GroupingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by veghe on 27/09/2016.
 */
@Repository

public class GroupingRepositoryImplementation implements GroupingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Grouping get(Long key) {
        return entityManager.find(Grouping.class, key);
    }

    @Override
    public Grouping update(Grouping grouping) {
        entityManager.merge(grouping);
        return grouping;
    }

    @Override
    public Grouping delete(Long key) {
        Grouping grouping = get(key);
        entityManager.remove(entityManager.contains(grouping) ? grouping : entityManager.merge(grouping));
        return grouping;
    }

    @Override
    public Grouping create(Grouping grouping) {
        entityManager.persist(grouping);
        return grouping;
    }

    @Override
    public List<Grouping> findAll() {
        Query query = entityManager.createQuery("from Grouping");
        return query.getResultList();
    }

    @Override
    public List<Grouping> findByUser(User user) {
        Query query = entityManager.createQuery("from Grouping where USER_IDENTIFIER = :id");
        query.setParameter("id", user.getIdentifier());
        return query.getResultList();
    }
}
