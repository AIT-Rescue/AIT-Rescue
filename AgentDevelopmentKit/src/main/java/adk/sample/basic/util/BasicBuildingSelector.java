package adk.sample.basic.util;

import adk.team.util.BuildingSelector;
import adk.team.util.PositionUtil;
import adk.team.util.provider.WorldProvider;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.HashSet;
import java.util.Set;

public class BasicBuildingSelector implements BuildingSelector {

    public WorldProvider<? extends StandardEntity> provider;

    public Set<Building> buildingList;

    public BasicBuildingSelector(WorldProvider<? extends StandardEntity> user) {
        this.provider = user;
        this.buildingList = new HashSet<>();
    }

    @Override
    public void add(Building building) {
        if (building.isOnFire()) {
            this.buildingList.add(building);
        }
        else {
            this.buildingList.remove(building);
        }
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
        this.buildingList.remove(building);
    }

    @Override
    public void remove(EntityID id) {
        StandardEntity entity = this.provider.getWorld().getEntity(id);
        if(entity instanceof Building) {
            this.buildingList.remove(entity);
        }
    }

    @Override
    public EntityID getTarget(int time) {
        Building result = null;
        StandardEntity owner = this.provider.getOwner();
        StandardWorldModel world = this.provider.getWorld();
        for(Building building : this.buildingList) {
            if(result != null) {
                result = (Building) PositionUtil.getNearEntity(owner, result, building, world);
            }
            else {
                result = building;
            }
        }
        return result != null ? result.getID() : null;
    }
}
