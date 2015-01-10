package comlib.message.information;

import comlib.message.MessageHuman;
import comlib.message.MessageID;
import rescuecore2.standard.entities.AmbulanceTeam;
import rescuecore2.worldmodel.EntityID;


public class MessageAmbulanceTeam extends MessageHuman
{
	/* below id is same to information.MessageAmbulanceTeam */
	public static final int ACTION_REST = 0;
	public static final int ACTION_MOVE = 1;
	public static final int ACTION_RESCUE = 2;
	public static final int ACTION_LOAD = 3;

	protected int rawTargetID;
	protected EntityID myTargetID;
	private int myAction;

	public MessageAmbulanceTeam(AmbulanceTeam ambulanceTeam, int action, EntityID target)
	{
		super(MessageID.ambulanceTeamMessage, ambulanceTeam);
		this.myTargetID = target;
		this.myAction = action;
	}

	public MessageAmbulanceTeam(int time, int ttl, int hp, int buriedness, int damage, int position, int id, int target, int action)
	{
		super(MessageID.ambulanceTeamMessage, time, ttl, hp, buriedness, damage, position, id);
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

