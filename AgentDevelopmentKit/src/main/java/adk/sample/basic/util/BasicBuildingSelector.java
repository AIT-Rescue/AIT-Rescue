package adk.sample.basic.util;

import adk.team.util.BuildingSelector;
import adk.team.util.provider.WorldProvider;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.EntityID;

import java.util.HashSet;
import java.util.Set;

public class BasicBuildingSelector implements BuildingSelector {

    public WorldProvider<? extends Human> provider;

    public Set<EntityID> buildingList;

    public BasicBuildingSelector(WorldProvider<? extends Human> user) {
        this.provider = user;
        this.buildingList = new HashSet<>();
    }

    @Override
    public void add(Building building) {
        if (building.isOnFire()) {
            this.buildingList.add(building.getID());
        }
        else {
            this.buildingList.remove(building.getID());
        }
        //if(building.getFieryness() > 0) {
        //}
    }

    @Override
    public void add(EntityID id) {
        StandardEntity entity = this.provider.getWorld().getEntity(id);
        if(entity instanceof Building) {
            this.add((Building)entity);
        }
    }

    @Override
    public void remove(Building building) {
        this.buildingList.remove(building.getID());
    }

    @Override
    public void remove(EntityID id) {
        this.buildingList.remove(id);
    }

    @Override
    public EntityID getTarget(int time) {
        EntityID result = null;
        int minDistance = Integer.MAX_VALUE;
        for (EntityID id : this.buildingList) {
            StandardEntity building = this.provider.getWorld().getEntity(id);
            if(building != null) {
                int d = this.provider.getWorld().getDistance(this.provider.me(), building);
                if (minDistance >= d) {
                    minDistance = d;
                    result = id;
                }
            }
        }
        return result;
    }
}
