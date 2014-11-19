package comlib.provider.information;

import comlib.provider.HumanMessageProvider;

import comlib.event.information.AmbulanceTeamMessageEvent;
import comlib.message.information.AmbulanceTeamMessage;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import rescuecore2.worldmodel.EntityID;


public class AmbulanceTeamMessageProvider extends HumanMessageProvider<AmbulanceTeamMessage, AmbulanceTeamMessageEvent>
{
	public AmbulanceTeamMessageProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, AmbulanceTeamMessage msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getHumanID().getValue(), config.getSizeOfAmbulanceTeamID());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, AmbulanceTeamMessage msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected AmbulanceTeamMessage createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new AmbulanceTeamMessage(time, -1,
				bsr.getBits(config.getSizeOfHumanHP()),
				bsr.getBits(config.getSizeOfHumanBuriedness()),
				bsr.getBits(config.getSizeOfHumanDamage()),
				bsr.getBits(config.getSizeOfHumanPosition()),
				bsr.getBits(config.getSizeOfAmbulanceTeamID())
				);
	}

	protected AmbulanceTeamMessage createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new AmbulanceTeamMessage(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

}
