package ns;

import rescuecore2.standard.entities.StandardEntity;

public interface WorldEvent<E extends StandardEntity> {
    public void event(E entity);
}
