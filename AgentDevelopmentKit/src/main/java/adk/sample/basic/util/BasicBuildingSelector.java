package adk.sample.basic.util;

import adk.team.util.BuildingSelector;
import adk.team.util.graph.PositionUtil;
import adk.team.util.provider.WorldProvider;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.StandardEntity;
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
        StandardEntity result = PositionUtil.getNearTarget(this.provider.getWorld(), this.provider.getOwner(), this.buildingList);
        return result != null ? result.getID() : null;
    }
}
