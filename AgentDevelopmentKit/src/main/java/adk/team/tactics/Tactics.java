package adk.team.tactics;

import adk.team.action.Action;
import comlib.manager.MessageManager;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.Refuge;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;
import java.util.Random;

public abstract class Tactics<E extends StandardEntity> implements WorldProvider<E> {

    public StandardWorldModel model;
    public EntityID agentID;
    public StandardEntity location;
    public int time;
    public EntityID target;
    public List<Refuge> refugeList;

    public abstract String getTacticsName();

    public abstract void preparation(Config config);

    public abstract void registerEvent(MessageManager manager);

    public abstract Action think(int time, ChangeSet changed, MessageManager manager);

    public void ignoreTimeThink(ChangeSet changed, MessageManager manager) {
    }

    public void registerProvider(MessageManager manager) {
    }

    @Override
    public EntityID getAgentID() {
        return this.agentID;
    }

    @Override
    public StandardWorldModel getWorld() {
        return this.model;
    }
}
