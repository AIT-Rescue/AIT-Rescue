package comlib.message.information;

import comlib.message.MessageHuman;
import comlib.message.MessageID;
import rescuecore2.standard.entities.FireBrigade;
import rescuecore2.worldmodel.EntityID;


public class MessageFireBrigade extends MessageHuman
{
	/* below id is same to information.MessageFireBrigade */
	public static final int ACTION_REST = 0;
	public static final int ACTION_MOVE = 1;
	public static final int ACTION_EXTINGUISH = 2;
	public static final int ACTION_REFILL = 3;

	protected int rawTargetID;
	protected EntityID myTargetID;
	private int fireBrigadeWater;
	private int myAction;

	public MessageFireBrigade(FireBrigade fireBrigade, int action, EntityID target)
	{
		super(MessageID.fireBrigadeMessage, fireBrigade);
		this.fireBrigadeWater = fireBrigade.getWater();
		this.myTargetID = target;
		this.myAction = action;
	}

	public MessageFireBrigade(int time, int ttl, int hp, int buriedness, int damage, int position, int id, int water, int target, int action)
	{
		super(MessageID.fireBrigadeMessage, time, ttl, hp, buriedness, damage, position, id);
		this.fireBrigadeWater = water;
		this.rawTargetID = target;
		this.myAction = action;
	}

	public int getWater() { return this.fireBrigadeWater; }

	public int getAction()
	{ return myAction; }

	public EntityID getTargetID()
	{
		if ( myTargetID == null )
		{ myTargetID = new EntityID(rawTargetID); }
		return myTargetID;
	}
}

