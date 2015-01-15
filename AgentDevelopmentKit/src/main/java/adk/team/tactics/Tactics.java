package adk.team.tactics;

import adk.team.action.Action;
import adk.team.util.provider.WorldProvider;
import comlib.manager.MessageManager;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.Refuge;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public abstract class Tactics<E extends StandardEntity> implements WorldProvider<E> {

    public boolean pre;

    public StandardWorldModel world;
    public StandardWorldModel model;
    public int time;
    public ChangeSet changed;

    public EntityID agentID;
    public StandardEntity location;

    public List<Refuge> refugeList;
    public EntityID target;

    public abstract String getTacticsName();

    public abstract void preparation(Config config);

    public abstract void registerEvent(MessageManager manager);

    public abstract Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager);

    public void ignoreTimeThink(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
    }

    public void registerProvider(MessageManager manager) {
    }

    @Override
    public int getCurrentTime() {
        return this.time;
    }

    @Override
    public ChangeSet getUpdateWorldData() {
        return this.changed;
    }

    @Override
    public StandardWorldModel getWorld() {
        return this.world;
    }

    @Override
    public EntityID getOwnerID() {
        return this.agentID;
    }

    public StandardEntity getOwnerLocation() {
        return this.location;
    }

    @Override
    public List<Refuge> getRefugeList() {
        return this.refugeList;
    }
}
