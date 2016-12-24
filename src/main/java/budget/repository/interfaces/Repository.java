package budget.repository.interfaces;

import java.util.List;

/**
 * Created by veghe on 12/08/2016.
 */
public interface Repository<Key, Entity> {

    Entity get(Key key);

    Entity update(Entity entity);

    Entity delete(Key key);

    Entity create(Entity entity);

    List<Entity> findAll();

}
