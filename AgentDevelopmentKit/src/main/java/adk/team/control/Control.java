package adk.team.control;

import adk.team.util.provider.WorldProvider;
import comlib.manager.MessageManager;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Refuge;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public abstract class Control<E extends Building> implements WorldProvider<E> {

    public boolean pre;

    public Config config;

    public StandardWorldModel world;
    public StandardWorldModel model;
    public int time;
    public ChangeSet changed;

    public EntityID stationID;

    public List<Refuge> refugeList;

    public abstract String getControlName();

    public abstract void preparation(Config config);

    public abstract void registerEvent(MessageManager manager);

    public abstract void think(int currentTime, ChangeSet updateWorldData, MessageManager manager);

    public void ignoreTimeThink(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
    }

    public void registerProvider(MessageManager manager) {
    }

    @Override
    public Config getConfig() {
        return this.config;
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
        return this.stationID;
    }

    @Override
    public List<Refuge> getRefugeList() {
        return this.refugeList;
    }
}
