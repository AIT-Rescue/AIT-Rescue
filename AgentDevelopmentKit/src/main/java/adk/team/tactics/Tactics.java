package adk.team.tactics;

import adk.team.action.Action;
import comlib.manager.MessageManager;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.Random;

public abstract class Tactics<E extends StandardEntity> {

    public StandardWorldModel model;
    public EntityID agentID;
    public StandardEntity location;
    public int time;
    public EntityID target;

    public Random random;
    public Config config;

    public abstract void preparation();

    public abstract void registerEvent(MessageManager manager);

    public abstract Action think(int time, ChangeSet changed, MessageManager manager);

    public abstract E me();

    public void ignoreTimeThink(int time, ChangeSet changed, MessageManager manager) {
    }

    public void registerProvider(MessageManager manager) {
    }

    public Config getConfig() {
        return this.config;
    }

    public StandardWorldModel getModel() {
        return this.model;
    }

    public StandardEntity location() {
        return this.location;
    }

    public EntityID getID() {
        return this.agentID;
    }
}
