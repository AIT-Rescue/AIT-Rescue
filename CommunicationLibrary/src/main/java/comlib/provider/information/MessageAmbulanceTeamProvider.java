package comlib.provider.information;

import comlib.provider.HumanMessageProvider;

import comlib.event.information.MessageAmbulanceTeamEvent;
import comlib.message.information.MessageAmbulanceTeam;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import rescuecore2.worldmodel.EntityID;


public class MessageAmbulanceTeamProvider extends HumanMessageProvider<MessageAmbulanceTeam, MessageAmbulanceTeamEvent>
{
	public MessageAmbulanceTeamProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, MessageAmbulanceTeam msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getHumanID().getValue(), config.getSizeOfAmbulanceTeamID());
		bos.writeBits(msg.getTargetID().getValue(), 32);
		bos.writeBits(msg.getAction(), 2);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, MessageAmbulanceTeam msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected MessageAmbulanceTeam createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new MessageAmbulanceTeam(time, -1,
				bsr.getBits(config.getSizeOfHumanHP()),
				bsr.getBits(config.getSizeOfHumanBuriedness()),
				bsr.getBits(config.getSizeOfHumanDamage()),
				bsr.getBits(config.getSizeOfHumanPosition()),
				bsr.getBits(config.getSizeOfAmbulanceTeamID()),
				bsr.getBits(32),
				bsr.getBits(2)
				);
	}

	protected MessageAmbulanceTeam createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new MessageAmbulanceTeam(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

}
