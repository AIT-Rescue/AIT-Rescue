package adk.team.precompute;

import adk.team.tactics.TacticsPolice;
import rescuecore2.standard.entities.PoliceForce;

public abstract class PrecomputePolice extends TacticsPolice {

	public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
		return new ActionRest(this);
	}

}
