package comlib.message.information;

import comlib.message.MessageHuman;
import comlib.message.MessageID;
import rescuecore2.standard.entities.PoliceForce;
import rescuecore2.worldmodel.EntityID;


public class MessagePoliceForce extends MessageHuman
{
	/* below id is same to information.MessagePoliceForce */
	public static final int ACTION_REST = 0;
	public static final int ACTION_MOVE = 1;
	public static final int ACTION_CLEAR = 2;

	protected int rawTargetID;
	protected EntityID myTargetID;
	private int myAction;

	public MessagePoliceForce(PoliceForce policeForce, int action, EntityID target)
	{
		super(MessageID.policeForceMessage, policeForce);
		this.myTargetID = target;
		this.myAction = action;
	}

	public MessagePoliceForce(int time, int ttl, int hp, int buriedness, int damage, int position, int id, int target, int action)
	{
		super(MessageID.policeForceMessage, time, ttl, hp, buriedness, damage, position, id);
		this.rawTargetID = target;
		this.myAction = action;
	}

	public int getAction()
	{ return myAction; }

	public EntityID getTargetID()
	{
		if ( myTargetID == null )
		{ myTargetID = new EntityID(rawTargetID); }
		return myTargetID;
	}
}

