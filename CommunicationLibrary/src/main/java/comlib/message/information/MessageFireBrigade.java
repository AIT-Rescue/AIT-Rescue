package comlib.message.information;

import comlib.message.MessageHuman;
import comlib.message.MessageID;
import rescuecore2.standard.entities.FireBrigade;
import rescuecore2.standard.entities.Human;


public class MessageFireBrigade extends MessageHuman
{
	private int fireBrigadeWater;

	public MessageFireBrigade(FireBrigade fireBrigade)
	{
		super(MessageID.fireBrigadeMessage, (Human)fireBrigade);
		fireBrigadeWater = fireBrigade.getWater();
	}

	public MessageFireBrigade(int time, int ttl, int hp, int buriedness, int damage, int position, int id, int water)
	{
		super(MessageID.fireBrigadeMessage, time, ttl, hp, buriedness, damage, position, id);
		this.fireBrigadeWater = water;
	}

	public int getWater() { return this.fireBrigadeWater; }
}

