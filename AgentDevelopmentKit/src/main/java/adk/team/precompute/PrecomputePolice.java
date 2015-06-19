package adk.team.precompute;

import adk.team.action.Action;
import adk.team.action.ActionRest;
import comlib.manager.MessageManager;
import rescuecore2.worldmodel.ChangeSet;
import adk.team.tactics.TacticsPolice;
import rescuecore2.standard.entities.PoliceForce;

public abstract class PrecomputePolice extends TacticsPolice {

	public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
		return new ActionRest(this);
	}

	@Override
	public void registerEvent(MessageManager manager) {
	}

}
