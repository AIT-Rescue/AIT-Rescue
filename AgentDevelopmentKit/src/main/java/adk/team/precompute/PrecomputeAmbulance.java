package adk.team.precompute;

import adk.team.tactics.TacticsAmbulance;
import rescuecore2.standard.entities.AmbulanceTeam;

public abstract class PrecomputeAmbulance extends TacticsAmbulance {

	public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
		return new ActionRest(this);
	}

}
