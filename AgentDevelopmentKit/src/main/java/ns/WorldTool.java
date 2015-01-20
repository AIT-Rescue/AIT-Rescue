package ns;

import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.standard.entities.StandardWorldModel;

public class WorldTool {

    private StandardWorldModel world;

    public WorldTool(StandardWorldModel standardWorldModel) {
        this.world = standardWorldModel;
    }

    public <E extends StandardEntity> void getEntity(WorldEvent<E> event) {
        for(StandardEntity entity : this.world.getEntitiesOfType(event.getType())) {
            try {
                event.event((E)entity);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
