package adk.team.precompute;

import adk.team.action.Action;
import adk.team.action.ActionRest;
import comlib.manager.MessageManager;
import rescuecore2.worldmodel.ChangeSet;
import adk.team.tactics.TacticsFire;
import rescuecore2.standard.entities.FireBrigade;

public abstract class PrecomputeFire extends TacticsFire {

	public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
		return new ActionRest(this);
	}

}
