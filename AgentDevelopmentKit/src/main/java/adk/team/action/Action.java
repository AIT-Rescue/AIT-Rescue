package adk.team.action;

import adk.team.tactics.Tactics;
import rescuecore2.messages.Message;
import rescuecore2.worldmodel.EntityID;

public abstract class Action<T extends Tactics> {

    public Action(T tactics) {
    }

    public abstract Message getCommand(EntityID agentID, int time);
}