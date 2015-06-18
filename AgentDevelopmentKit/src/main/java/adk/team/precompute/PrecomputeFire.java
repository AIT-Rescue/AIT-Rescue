package adk.team.precompute;

import adk.team.tactics.TacticsFire;
import rescuecore2.standard.entities.FireBrigade;

public abstract class PrecomputeFire extends TacticsFire {

	public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
		return new ActionRest(this);
	}

}
