package ns;

import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;

public interface WorldEvent<E extends StandardEntity> {

    //return Arrays.asList(StandardEntityURN... types);
    public StandardEntityURN[] getType();

    public void event(E entity);
}
