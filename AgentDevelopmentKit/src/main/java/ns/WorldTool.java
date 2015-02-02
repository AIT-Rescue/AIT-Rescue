package ns;

import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;

public class WorldTool {
    //test
    @SuppressWarnings("unchecked")
    public static <E extends StandardEntity> void getEntity(StandardWorldModel world, WorldEvent<E> event) {
        for(StandardEntity entity : world.getEntitiesOfType(event.getType())) {
            try {
                event.event((E)entity);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
