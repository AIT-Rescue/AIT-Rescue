package comlib.message.information;

import comlib.message.MessageHuman;

import comlib.message.MessageID;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.entities.Human;


public class MessageCivilian extends MessageHuman
{
	public MessageCivilian(Civilian civilian)
	{
		super(MessageID.civilianMessage, (Human)civilian);
	}

	public MessageCivilian(int time, int ttl, int hp, int buriedness, int damage, int position, int id)
	{
		super(MessageID.civilianMessage, time, ttl, hp, buriedness, damage, position, id);
	}
}

