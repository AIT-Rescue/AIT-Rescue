package comlib.message.information;

import comlib.message.MessageMap;

import comlib.message.MessageID;
import rescuecore2.standard.entities.Building;
import rescuecore2.worldmodel.EntityID;


public class MessageBuilding extends MessageMap
{
	protected int rawBuildingID;
	protected EntityID buildingID;
	protected int buildingBrokenness;
	protected int buildingFieryness;

	public MessageBuilding(Building building)
	{
		super(MessageID.buildingMessage);
		this.buildingID = building.getID();
		this.buildingBrokenness  = building.getBrokenness();
		this.buildingFieryness  = building.getFieryness();
	}

	public MessageBuilding(int time, int ttl, int id, int brokenness, int fieryness)
	{
		super(MessageID.buildingMessage, time, ttl);
		this.rawBuildingID = id;
		this.buildingBrokenness = brokenness;
		this.buildingFieryness = fieryness;
	}

	public EntityID getBuildingID()
	{
		if (this.buildingID == null) {
            this.buildingID = new EntityID(this.rawBuildingID);
        }
		return this.buildingID;
	}

	public int getBrokenness() {
        return this.buildingBrokenness;
    }

	public int getFieryness() {
        return this.buildingFieryness;
    }
}

