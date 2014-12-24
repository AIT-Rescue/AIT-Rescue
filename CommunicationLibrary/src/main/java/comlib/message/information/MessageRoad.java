package comlib.message.information;

import comlib.message.MessageMap;
import comlib.message.MessageID;
import rescuecore2.standard.entities.Road;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.worldmodel.EntityID;

public class MessageRoad extends MessageMap
{
	protected int rawRoadID;
	protected int rawBlockadeID;
	protected EntityID roadID;
	protected EntityID roadBlockadeID;
	protected int blockadeRepairCost;
	protected boolean roadPassable;

	public MessageRoad(Road road, Blockade blockade, boolean isPassable)
	{
		super(MessageID.roadMessage);
		this.roadID = road.getID();
		this.roadBlockadeID = blockade.getID();
		this.blockadeRepairCost = blockade.getRepairCost();
		this.roadPassable = isPassable;
	}

	public MessageRoad(int time, int ttl, int id, int blockadeID, int repairCost, boolean isPassable)
	{
		super(MessageID.roadMessage, time, ttl);
		this.rawRoadID = id;
		this.rawBlockadeID = blockadeID;
		this.blockadeRepairCost = repairCost;
		this.roadPassable = isPassable;
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

	public boolean isPassable()
	{
		return roadPassable;
	}
}

