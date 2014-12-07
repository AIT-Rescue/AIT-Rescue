package comlib.message.information;

import comlib.message.MapMessage;
import comlib.message.MessageID;
import rescuecore2.standard.entities.Road;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.worldmodel.EntityID;

public class RoadMessage extends MapMessage
{
	protected int rawRoadID;
	protected int rawBlockadeID;
	protected EntityID roadID;
	protected EntityID roadBlockadeID;
	protected int blockadeRepairCost;

	public RoadMessage(Road road, Blockade blockade)
	{
		super(MessageID.roadMessage);
		this.roadID = road.getID();
		this.roadBlockadeID = blockade.getID();
		this.blockadeRepairCost = blockade.getRepairCost();
	}

	public RoadMessage(int time, int ttl, int id, int blockadeID, int repairCost)
	{
		super(MessageID.roadMessage, time, ttl);
		this.rawRoadID = id;
		this.rawBlockadeID = blockadeID;
		this.blockadeRepairCost = repairCost;
	}

	public EntityID getRoadID()
	{
		if (this.roadID == null)
		{ this.roadID = new EntityID(this.rawRoadID); }

		return this.roadID;
	}

	public EntityID getBlockadeID()
	{
		if (this.roadBlockadeID == null)
		{ this.roadBlockadeID = new EntityID(this.rawBlockadeID); }

		return this.roadBlockadeID;
	}

	public int getRepairCost()
	{
		return blockadeRepairCost;
	}
}

