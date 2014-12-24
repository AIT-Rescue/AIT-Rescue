package comlib.message.information;

import comlib.message.MessageHuman;
import comlib.message.MessageID;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.PoliceForce;


public class MessagePoliceForce extends MessageHuman
{
	public MessagePoliceForce(PoliceForce policeForce)
	{
		super(MessageID.policeForceMessage, (Human)policeForce);
	}

	public MessagePoliceForce(int time, int ttl, int hp, int buriedness, int damage, int position, int id)
	{
		super(MessageID.policeForceMessage, time, ttl, hp, buriedness, damage, position, id);
	}
}

