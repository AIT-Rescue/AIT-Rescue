package comlib.message.information;

import comlib.message.HumanMessage;
import comlib.message.MessageID;
import rescuecore2.standard.entities.AmbulanceTeam;
import rescuecore2.standard.entities.Human;


public class AmbulanceTeamMessage extends HumanMessage
{
	public AmbulanceTeamMessage(AmbulanceTeam ambulanceTeam)
	{
		super(MessageID.ambulanceTeamMessage, (Human)ambulanceTeam);
	}

	public AmbulanceTeamMessage(int time, int ttl, int hp, int buriedness, int damage, int position, int id)
	{
		super(MessageID.ambulanceTeamMessage, time, ttl, hp, buriedness, damage, position, id);
	}
}

