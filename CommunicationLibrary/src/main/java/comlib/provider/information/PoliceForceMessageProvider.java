package comlib.provider.information;

import comlib.provider.HumanMessageProvider;

import comlib.event.information.PoliceForceMessageEvent;
import comlib.message.information.PoliceForceMessage;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import rescuecore2.worldmodel.EntityID;


public class PoliceForceMessageProvider extends HumanMessageProvider<PoliceForceMessage, PoliceForceMessageEvent>
{
	public PoliceForceMessageProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, PoliceForceMessage msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getHumanID().getValue(), config.getSizeOfPoliceForceID());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, PoliceForceMessage msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected PoliceForceMessage createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new PoliceForceMessage(time, -1,
				bsr.getBits(config.getSizeOfHumanHP()),
				bsr.getBits(config.getSizeOfHumanBuriedness()),
				bsr.getBits(config.getSizeOfHumanDamage()),
				bsr.getBits(config.getSizeOfHumanPosition()),
				bsr.getBits(config.getSizeOfPoliceForceID())
				);
	}

	protected PoliceForceMessage createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new PoliceForceMessage(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

}
