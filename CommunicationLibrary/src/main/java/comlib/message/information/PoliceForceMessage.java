package comlib.message.information;

import comlib.message.HumanMessage;
import comlib.message.MessageID;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.PoliceForce;


public class PoliceForceMessage extends HumanMessage
{
	public PoliceForceMessage(PoliceForce policeForce)
	{
		super(MessageID.policeForceMessage, (Human)policeForce);
	}

	public PoliceForceMessage(int time, int ttl, int hp, int buriedness, int damage, int position, int id)
	{
		super(MessageID.policeForceMessage, time, ttl, hp, buriedness, damage, position, id);
	}
}

